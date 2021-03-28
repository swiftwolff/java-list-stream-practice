

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import file.MovieDB;
import movies.Actor;
import movies.Movie;


class MovieTriviaTest {
	
	//instance of movie trivia object to test
	MovieTrivia mt;
	
	@BeforeEach
	void setUp() throws Exception {
		//initialize movie trivia object
		mt = new MovieTrivia ();
		
		//set up movie trivia object
		mt.setUp("moviedata.txt", "movieratings.csv");
	}

	@Test
	void testSetUp() { 
		assertEquals(mt.actorsInfo.size(), 6);
		assertEquals(mt.moviesInfo.size(), 7);
		
		assertEquals(mt.actorsInfo.get(0).getName(), "meryl streep");
		assertEquals(mt.actorsInfo.get(0).getMoviesCast().size(), 3);
		assertEquals(mt.actorsInfo.get(0).getMoviesCast().get(0), "doubt");
		
		assertEquals(mt.moviesInfo.get(0).getName(), "doubt");
		assertEquals(mt.moviesInfo.get(0).getCriticRating(), 79);
		assertEquals(mt.moviesInfo.get(0).getAudienceRating(), 78);
	}
	
	@Test
	void testInsertActor () {
		mt.insertActor("test1", new String [] {"testmovie1", "testmovie2"}, mt.actorsInfo);
		System.out.println(mt.actorsInfo.size());
		assertEquals(mt.actorsInfo.size(), 7);	
		assertEquals(mt.actorsInfo.get(mt.actorsInfo.size() - 1).getName(), "test1");
		assertEquals(mt.actorsInfo.get(mt.actorsInfo.size() - 1).getMoviesCast().size(), 2);
		assertEquals(mt.actorsInfo.get(mt.actorsInfo.size() - 1).getMoviesCast().get(0), "testmovie1");
		
		// Should not create new actor if the actor already exists in the actorsInfo
		mt.insertActor("tom hanks", new String [] {"testmovie1", "testmovie2"}, mt.actorsInfo);
		assertEquals(mt.actorsInfo.size(), 7);

		mt.insertActor("DEREK YUEH", new String [] {"testmovie1", "testmovie2"}, mt.actorsInfo);
		assertEquals(mt.actorsInfo.size(), 8);
		Actor a = mt.actorsInfo
		.stream()
		.filter(actor -> "derek yueh".equals(actor.getName()))
		.findFirst()
		.orElse(null);
		assertNotNull(a);
	}
	
	@Test
	void testInsertRating () {
		mt.insertRating("testmovie", new int [] {79, 80}, mt.moviesInfo);
		assertEquals(mt.moviesInfo.size(), 8);	
		assertEquals(mt.moviesInfo.get(mt.moviesInfo.size() - 1).getName(), "testmovie");
		assertEquals(mt.moviesInfo.get(mt.moviesInfo.size() - 1).getCriticRating(), 79);
		assertEquals(mt.moviesInfo.get(mt.moviesInfo.size() - 1).getAudienceRating(), 80);
		
		mt.insertRating("jaws", new int [] {0, 0}, mt.moviesInfo);
		assertEquals(mt.moviesInfo.size(), 8);

		Movie m = mt.moviesInfo
		.stream()
		.filter(movie -> "jaws".equals(movie.getName()))
		.findAny()
		.orElse(null);

		assertNotNull(m);
		assertEquals(m.getCriticRating(), 0);
		assertEquals(m.getAudienceRating(), 0);
	}
	
	@Test
	void testSelectWhereActorIs () {
		assertEquals(mt.selectWhereActorIs("meryl streep", mt.actorsInfo).size(), 3);
		assertEquals(mt.selectWhereActorIs("meryl streep", mt.actorsInfo).get(0), "doubt");
		assertEquals(mt.selectWhereActorIs("somebody", mt.actorsInfo).size(), 0);
	}
	
	@Test
	void testSelectWhereMovieIs () {
		assertEquals(mt.selectWhereMovieIs("doubt", mt.actorsInfo).size(), 2);
		assertEquals(mt.selectWhereMovieIs("doubt", mt.actorsInfo).contains("meryl streep"), true);
		assertEquals(mt.selectWhereMovieIs("doubt", mt.actorsInfo).contains("amy adams"), true);
		assertEquals(mt.selectWhereMovieIs("dOUbt", mt.actorsInfo).contains("amy adams"), true);
		assertEquals(mt.selectWhereMovieIs("No one knowS", mt.actorsInfo).size(), 0);
	}
	
	@Test
	void testSelectWhereRatingIs () {
		assertEquals(mt.selectWhereRatingIs('>', 0, true, mt.moviesInfo).size(), 6);
		assertEquals(mt.selectWhereRatingIs('=', 65, false, mt.moviesInfo).size(), 0);
		assertEquals(mt.selectWhereRatingIs('<', 30, true, mt.moviesInfo).size(), 2);
		
		assertEquals(mt.selectWhereRatingIs('a', 20, false, mt.moviesInfo).size(), 0);
	}
	
	@Test
	void testGetCoActors () {
		assertEquals(mt.getCoActors("meryl streep", mt.actorsInfo).size(), 2);
		assertTrue(mt.getCoActors("meryl streep", mt.actorsInfo).contains("tom hanks"));
		assertTrue(mt.getCoActors("meryl streep", mt.actorsInfo).contains("amy adams"));
		assertEquals(mt.getCoActors("MerYl strEEp", mt.actorsInfo).size(), 2);
	}
	
	@Test
	void testGetCommonMovie () {
		assertEquals(mt.getCommonMovie("meryl streep", "tom hanks", mt.actorsInfo).size(), 1);
		assertTrue(mt.getCommonMovie("meryl streep", "tom hanks", mt.actorsInfo).contains("the post"));
		
		assertEquals(mt.getCommonMovie("robin williams", "brad pitt", mt.actorsInfo).size(), 0);
		assertEquals(mt.getCommonMovie("mERyl stReeP", "tom hanks", mt.actorsInfo).size(), 1);
	}
	
	@Test
	void testGoodMovies () {
		assertEquals(mt.goodMovies(mt.moviesInfo).size(), 3);
		assertTrue(mt.goodMovies(mt.moviesInfo).contains("jaws"));
		assertTrue(mt.goodMovies(mt.moviesInfo).contains("et"));
	}
	
	@Test
	void testGetCommonActors () {
		assertEquals(mt.getCommonActors("doubt", "the post", mt.actorsInfo).size(), 1);
		assertTrue(mt.getCommonActors("doubt", "the post", mt.actorsInfo).contains("meryl streep"));
		assertTrue(mt.getCommonActors("CasT away", "the Post", mt.actorsInfo).contains("tom hanks"));
	}
	
	@Test
	void testGetMean () {
		assertEquals(MovieTrivia.getMean(mt.moviesInfo)[0], 67.85714285714286);
		assertEquals(MovieTrivia.getMean(mt.moviesInfo)[1], 65.71428571428571);
	}
}
