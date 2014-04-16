package mcts.integration.stub.domain;
import static junit.framework.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

import mcts.integration.stub.domain.Update;

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
	public void shouldTestVerify(){
		String date1 = update.verifyAsOnDate("12-13-2014");
		String date2 = update.verifyAsOnDate("12-12-2014");
		String date3 = update.verifyAsOnDate("32-12-2014");
		assertEquals("invalid date", date1);
		assertEquals("invalid date", date3);
		assertEquals(null, date2);
		
	}
	
	@Test
	public void shouldVerifyStateId() {
		assertEquals("invalid StateId", update.verifyStateId(12));
		assertEquals(null, update.verifyStateId(10));
	}

	@Test
	public void shouldVerifyMctsId() {
		assertEquals(null, update.verifyMctsId("12"));
		assertEquals("Invalid Mcts-Id", update.verifyMctsId(null));
	}

	@Test
	public void shouldVerifyServiceType() {

		assertEquals(null, update.verifyServiceType(3));
		assertEquals("invalid Service Type", update.verifyServiceType(13));
		assertEquals("invalid Service Type", update.verifyServiceType(1));
		assertEquals("invalid Service Type", update.verifyServiceType(10));
	}
	@Test
	public void shouldVerifyDayOffSet() {
		assertEquals(null, update.verifyDayOffSet(0));
		assertEquals("invalid Day OffSet", update.verifyDayOffSet(2));
		
	}

	@Test
	public void shouldVerifyContactNo() {
		assertEquals(null, update.verifyContactNo("0"));
		
	}

/*	// Verifies it to be either 1 or 2 or 3 or null
	private String verifyHbLevel(String hbLevel) {
		if (this.serviceType == 2 || this.serviceType == 3
				|| this.serviceType == 4) {
			if (hbLevel.equals("1") || hbLevel.equals("2")
					|| hbLevel.equals("3") || hbLevel.equals("")
					|| hbLevel == null)
				return null;
		} else if (hbLevel == "" || hbLevel == null)
			return null;
		return "invalid hb Level";
	}
*/
	@Test
	public void shouldVerifyMode() {
		assertEquals(null, update.verifyMode(4));
		assertEquals("invalid mode", update.verifyMode(2));
	}
	
}
