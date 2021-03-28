import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import file.MovieDB;
import movies.Actor;
import movies.Movie;

/**
 * Movie trivia class providing different methods for querying and updating a movie database.
 */
public class MovieTrivia {
	
	/**
	 * List of actors information.
	 */
	public ArrayList<Actor> actorsInfo = new ArrayList<Actor>();
	
	/**
	 * List of movies information.
	 */
	public ArrayList<Movie> moviesInfo = new ArrayList<Movie>();
	
	
	public static void main(String[] args) {
		
		//create instance of movie trivia class
		MovieTrivia mt = new MovieTrivia();
		
		//setup movie trivia class
		mt.setUp("moviedata.txt", "movieratings.csv");
	}

	public void insertActor(String actor, String[] movies, ArrayList<Actor> actorsInfo) {
		String actorNameLowerCase = actor.toLowerCase();
		ArrayList <String> moviesCasted = new ArrayList<>();
		Actor a = actorsInfo
		.stream()
		.filter(listedActor -> actorNameLowerCase.equals(listedActor.getName()))
		.findFirst()
		.orElse(null);

		if (a != null) {
			moviesCasted = a.getMoviesCast();
		} else {
			a = new Actor(actorNameLowerCase);
			actorsInfo.add(a);
			moviesCasted = a.getMoviesCast();
		}

		for (String m : movies) {
			moviesCasted.add(m);
		}
	}

	public void insertRating(String movie, int[] ratings, ArrayList<Movie> moviesInfo) {
		String movieNameLowerCase = movie.toLowerCase();
		Movie m = moviesInfo
		.stream()
		.filter(listedMovie -> movie.equals(listedMovie.getName()))
		.findFirst()
		.orElse(null);

		if (m != null) {
			m.setCriticRating(ratings[0]);
			m.setAudienceRating(ratings[1]);
		} else {
			m = new Movie(movieNameLowerCase, ratings[0], ratings[1]);
			moviesInfo.add(m);
		}
	}

	public ArrayList<String> selectWhereActorIs(String actor, ArrayList<Actor> actorsInfo) {
		Actor a = actorsInfo
		.stream()
		.filter(listedActor -> actor.equals(listedActor.getName()))
		.findFirst()
		.orElse(null);

		if (a != null) {
			return a.getMoviesCast();
		}
		return new ArrayList<String>();
	}

	public ArrayList<String> selectWhereMovieIs(String movie, ArrayList<Actor> actorsInfo) {
		List<String> actors = actorsInfo
		.stream()
		.filter(actor -> actor.getMoviesCast().contains(movie.toLowerCase()))
		.map(actorObj -> actorObj.getName())
		.collect(Collectors.toList());
		return new ArrayList<String>(actors);
	}

	public ArrayList <String> selectWhereRatingIs (char comparison, int targetRating, boolean isCritic, ArrayList <Movie> moviesInfo) {
		ArrayList<String> movies = new ArrayList<>();
		List<Movie> movieList = new ArrayList<>();
		if (isCritic) {
			switch(comparison) {
				case '>':
					movieList = moviesInfo
					.stream()
					.filter(movie -> movie.getCriticRating() > targetRating)
					.collect(Collectors.toList());
					break;
				case '<':
					movieList = moviesInfo
					.stream()
					.filter(movie -> movie.getCriticRating() < targetRating)
					.collect(Collectors.toList());
					break;
				case '=':
					movieList = moviesInfo
					.stream()
					.filter(movie -> movie.getCriticRating() == targetRating)
					.collect(Collectors.toList());
				  break;
			  }
		} else {
			switch(comparison) {
				case '>':
					movieList = moviesInfo
					.stream()
					.filter(movie -> movie.getAudienceRating() > targetRating)
					.collect(Collectors.toList());
					break;
				case '<':
					movieList = moviesInfo
					.stream()
					.filter(movie -> movie.getAudienceRating() < targetRating)
					.collect(Collectors.toList());
					break;
				case '=':
					movieList = moviesInfo
					.stream()
					.filter(movie -> movie.getAudienceRating() == targetRating)
					.collect(Collectors.toList());
				  break;
			  }
		}

		for (Movie m : movieList) {
			movies.add(m.getName());
		}
		return movies;
	}

	public ArrayList <String> getCommonMovie (String actor1, String actor2, ArrayList <Actor> actorsInfo) {
		Actor aOne = actorsInfo
		.stream()
		.filter(listedActor -> actor1.toLowerCase().equals(listedActor.getName()))
		.findAny()
		.orElse(null);

		Actor aTwo = actorsInfo
		.stream()
		.filter(listedActor -> actor2.toLowerCase().equals(listedActor.getName()))
		.findAny()
		.orElse(null);

		if (aOne == null || aTwo == null) {
			return new ArrayList<String>();
		}

		List<String> intersectMovieList = aOne
		.getMoviesCast()
		.stream()
		.filter(aTwo.getMoviesCast()::contains)
		.collect(Collectors.toList());
		return new ArrayList<String>(intersectMovieList);
	}

	public ArrayList <String> getCoActors (String actor, ArrayList <Actor> actorsInfo) {
		Set<String> coActors = new HashSet<String>();
		Actor a = actorsInfo
		.stream()
		.filter(listedActor -> actor.toLowerCase().equals(listedActor.getName()))
		.findFirst()
		.orElse(null);
		if (a == null) {
			return new ArrayList<String>();
		}
		
		for (Actor listedActor : actorsInfo) {
			if (listedActor.getName().equals(a.getName())) {
				continue;
			}
			ArrayList<String> movies = listedActor.getMoviesCast();
			List<String> coMovies = movies
			.stream()
			.filter(a.getMoviesCast()::contains)
			.collect(Collectors.toList());
			if (coMovies.size() > 0) {
				coActors.add(listedActor.getName());
			}
		}
		return new ArrayList<String>(coActors);
	}

	public ArrayList <String> goodMovies (ArrayList <Movie> moviesInfo) {
		ArrayList<String> goodCritics = selectWhereRatingIs('>', 85, true, moviesInfo);
		ArrayList<String> borderlineCritics = selectWhereRatingIs('=', 85, true, moviesInfo);
		goodCritics.addAll(borderlineCritics);
		ArrayList<String> goodAudRating = selectWhereRatingIs('>', 85, false, moviesInfo);
		ArrayList<String> borderlineAudRating = selectWhereRatingIs('=', 85, false, moviesInfo);
		goodAudRating.addAll(borderlineAudRating);
		List<String> intersectMovieList = goodCritics
		.stream()
		.filter(goodAudRating::contains)
		.collect(Collectors.toList());

		return new ArrayList<String>(intersectMovieList);
	}

	public ArrayList <String> getCommonActors (String movie1, String movie2, ArrayList <Actor> actorsInfo) {
		ArrayList<String> movie1Actors = selectWhereMovieIs(movie1, actorsInfo);
		ArrayList<String> movie2Actors = selectWhereMovieIs(movie2, actorsInfo);
		List<String> commonActors = movie1Actors
			.stream()
			.filter(movie2Actors::contains)
			.collect(Collectors.toList());
		return new ArrayList<String>(commonActors);
	}

	public static double [] getMean (ArrayList <Movie> moviesInfo) {
		int movieCnt = moviesInfo.size();
		double criticSum = moviesInfo
		.stream()
		.mapToDouble(m -> m.getCriticRating())
		.sum();
		double audRatingSum = moviesInfo
		.stream()
		.mapToDouble(m -> m.getAudienceRating())
		.sum();
		return new double[] {criticSum / movieCnt, audRatingSum / movieCnt};
	}

	public void setUp(String movieData, String movieRatings) {
		//create instance of movie database
		MovieDB movieDB = new MovieDB();
		
		//load movie database files
		movieDB.setUp(movieData, movieRatings);
		
		//get actor and movie data
		this.actorsInfo = movieDB.getActorsInfo();
		this.moviesInfo = movieDB.getMoviesInfo();
		
		//print all actors and movies
		this.printAllActors();
		this.printAllMovies();		
	}
	
	public void printAllActors () {
		System.out.println(actorsInfo);
	}
	
	public void printAllMovies () {
		System.out.println(moviesInfo);
	}
	
}
