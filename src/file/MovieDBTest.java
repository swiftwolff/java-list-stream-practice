package file;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovieDBTest {

	//instance of movie db to test
	MovieDB db;
	
	@BeforeEach
	void setUp() throws Exception {
		//initialize movie db
		this.db = new MovieDB ();
		
		//setup movie db
		this.db.setUp("moviedata.txt", "movieratings.csv");
	}

	@Test
	void testSetUp() { 
		assertEquals(this.db.getActorsInfo().size(), 6);
		assertEquals(this.db.getMoviesInfo().size(), 7);
		
		assertEquals(this.db.getActorsInfo().get(0).getName(), "meryl streep");
		assertEquals(this.db.getActorsInfo().get(0).getMoviesCast().size(), 3);
		assertEquals(this.db.getActorsInfo().get(0).getMoviesCast().get(0), "doubt");
		
		assertEquals(this.db.getMoviesInfo().get(0).getName(), "doubt");
		assertEquals(this.db.getMoviesInfo().get(0).getCriticRating(), 79);
		assertEquals(this.db.getMoviesInfo().get(0).getAudienceRating(), 78);
	}

}
