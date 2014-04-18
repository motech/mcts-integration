package mcts.integration.stub.domain;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.junit.Test;
public class UpdateTest {

	private Update update = new Update();
	private Date date = new Date(12, 12 , 2014);
	SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
	
	@Test
	public void shouldTestVerifyUpdate(){
		Update update1 = new Update(10, "221213", 3, date, "12211111", "2");
		assertEquals(null, update1.verifyUpdate());
	}
	
	@Test
	public void shouldTestVerifyAsOnDate(){
		String date1 = update.verifyAsOnDate("12-13-2014");
		String date2 = update.verifyAsOnDate("12-12-2014");
		String date3 = update.verifyAsOnDate("32-12-2014");
		assertEquals("invalid date: 12-13-2014", date1);
		assertEquals("invalid date: 32-12-2014", date3);
		assertEquals(null, date2);
		
	}
	
	@Test
	public void shouldVerifyStateId() {
		assertEquals("invalid StateId: 12", update.verifyStateId(12));
		assertEquals(null, update.verifyStateId(10));
	}

	@Test
	public void shouldVerifyMctsId() {
		assertEquals(null, update.verifyMctsId("12"));
		assertEquals("Invalid Mcts-Id: null", update.verifyMctsId(null));
	}

	@Test
	public void shouldVerifyServiceType() {

		assertEquals(null, update.verifyServiceType(3));
		assertEquals("invalid Service Type: 13", update.verifyServiceType(13));
		assertEquals("invalid Service Type: 1", update.verifyServiceType(1));
		assertEquals("invalid Service Type: 10", update.verifyServiceType(10));
	}
	@Test
	public void shouldVerifyDayOffSet() {
		assertEquals(null, update.verifyDayOffSet(0));
		assertEquals("invalid Day OffSet: 2", update.verifyDayOffSet(2));
		
	}

	@Test
	public void shouldVerifyContactNo() {
		assertEquals(null, update.verifyContactNo("0"));
		
	}

	@Test
	public void verifyHbLevel() {
		update.setServiceType(2);
		assertEquals(null, update.verifyHbLevel("1"));
		assertEquals(null, update.verifyHbLevel("2"));
		assertEquals(null, update.verifyHbLevel("3"));
		assertEquals(null, update.verifyHbLevel(""));
		assertEquals("invalid hb Level: 11", update.verifyHbLevel("11"));
		update.setServiceType(6);
		assertEquals("invalid hb Level: 1", update.verifyHbLevel("1"));
		assertEquals("invalid hb Level: 2", update.verifyHbLevel("2"));
		assertEquals("invalid hb Level: 3", update.verifyHbLevel("3"));
		assertEquals(null, update.verifyHbLevel(""));
		assertEquals("invalid hb Level: 11", update.verifyHbLevel("11"));
	}

	@Test
	public void shouldVerifyMode() {
		assertEquals(null, update.verifyMode(4));
		assertEquals("invalid mode: 2", update.verifyMode(2));
	}
	
}
