package org.motechproject.mcts.integration.repository;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.mcts.integration.hibernate.model.BpForm;

public class BpFormRepositoryIT extends BaseRepositoryIT {

	@After
	@Before
	public void shouldClearBpFormTable() {
		getCurrentSession().createSQLQuery("DELETE FROM report.bp_form")
				.executeUpdate();
	}
	
	@Test
	public void we(){
		System.out.println("runned");
	}

	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Test
	public void shouldGetAllBpForms() {
		List<BpForm> bpFormsToBeAdded = getListOfBpFormsWithAncHemoglobinCombinations();
		addBpFormsToCurrentSession(bpFormsToBeAdded);
		List<BpForm> bpFormsFromDb = getCurrentSession().createSQLQuery(
				"SELECT * FROM report.bp_form").list();
		assertEquals(bpFormsToBeAdded.size(), bpFormsFromDb.size());
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Test
	public void shouldGetBpFormsWithNotNullAncHemoglobin() {
		List<BpForm> bpFormsToBeAdded = getListOfBpFormsWithAncHemoglobinCombinations();
		addBpFormsToCurrentSession(bpFormsToBeAdded);
		List<BpForm> bpFormsFromDbWithNoNull = getCurrentSession()
				.createSQLQuery(
						"SELECT * FROM report.bp_form where anc1_hemoglobin IS NOT NULL AND anc2_hemoglobin IS NOT NULL AND anc3_hemoglobin IS NOT NULL AND anc4_hemoglobin IS NOT NULL")
				.list();
		assertEquals(1, bpFormsFromDbWithNoNull.size());
		List<BpForm> bpFormsFromDbWithNotNullAnc1 = getCurrentSession()
				.createSQLQuery(
						"SELECT * FROM report.bp_form where anc1_hemoglobin IS NOT NULL")
				.list();
		assertEquals(8, bpFormsFromDbWithNotNullAnc1.size());
		List<BpForm> bpFormsFromDbWithNotNullAnc2 = getCurrentSession()
				.createSQLQuery(
						"SELECT * FROM report.bp_form where anc2_hemoglobin IS NOT NULL")
				.list();
		assertEquals(8, bpFormsFromDbWithNotNullAnc2.size());
		List<BpForm> bpFormsFromDbWithNotNullAnc3 = getCurrentSession()
				.createSQLQuery(
						"SELECT * FROM report.bp_form where anc3_hemoglobin IS NOT NULL")
				.list();
		assertEquals(8, bpFormsFromDbWithNotNullAnc3.size());
		List<BpForm> bpFormsFromDbWithNotNullAnc4 = getCurrentSession()
				.createSQLQuery(
						"SELECT * FROM report.bp_form where anc4_hemoglobin IS NOT NULL")
				.list();
		assertEquals(8, bpFormsFromDbWithNotNullAnc4.size());
		List<BpForm> bpFormsFromDbWithNotNullAnc1_2 = getCurrentSession()
				.createSQLQuery(
						"SELECT * FROM report.bp_form where anc1_hemoglobin IS NOT NULL AND anc2_hemoglobin IS NOT NULL;")
				.list();
		assertEquals(4, bpFormsFromDbWithNotNullAnc1_2.size());
		List<BpForm> bpFormsFromDbWithNotNullAnc2_3 = getCurrentSession()
				.createSQLQuery(
						"SELECT * FROM report.bp_form where anc2_hemoglobin IS NOT NULL AND anc3_hemoglobin IS NOT NULL;")
				.list();
		assertEquals(4, bpFormsFromDbWithNotNullAnc2_3.size());
		List<BpForm> bpFormsFromDbWithNotNullAnc3_4 = getCurrentSession()
				.createSQLQuery(
						"SELECT * FROM report.bp_form where anc3_hemoglobin IS NOT NULL AND anc4_hemoglobin IS NOT NULL;")
				.list();
		assertEquals(4, bpFormsFromDbWithNotNullAnc3_4.size());
		List<BpForm> bpFormsFromDbWithNotNullAnc4_1 = getCurrentSession()
				.createSQLQuery(
						"SELECT * FROM report.bp_form where anc4_hemoglobin IS NOT NULL AND anc1_hemoglobin IS NOT NULL;")
				.list();
		assertEquals(4, bpFormsFromDbWithNotNullAnc4_1.size());
		List<BpForm> bpFormsFromDbWithNotNullAnc2_4 = getCurrentSession()
				.createSQLQuery(
						"SELECT * FROM report.bp_form where anc2_hemoglobin IS NOT NULL AND anc4_hemoglobin IS NOT NULL;")
				.list();
		assertEquals(4, bpFormsFromDbWithNotNullAnc2_4.size());
		List<BpForm> bpFormsFromDbWithNotNullAnc1_3 = getCurrentSession()
				.createSQLQuery(
						"SELECT * FROM report.bp_form where anc3_hemoglobin IS NOT NULL AND anc1_hemoglobin IS NOT NULL;")
				.list();
		assertEquals(4, bpFormsFromDbWithNotNullAnc1_3.size());
		List<BpForm> bpFormsFromDbWithNotNullAnc1_2_3 = getCurrentSession()
				.createSQLQuery(
						"SELECT * FROM report.bp_form where anc1_hemoglobin IS NOT NULL AND anc2_hemoglobin IS NOT NULL AND anc3_hemoglobin IS NOT NULL;")
				.list();
		assertEquals(2, bpFormsFromDbWithNotNullAnc1_2_3.size());
		List<BpForm> bpFormsFromDbWithNotNullAnc2_3_4 = getCurrentSession()
				.createSQLQuery(
						"SELECT * FROM report.bp_form where anc2_hemoglobin IS NOT NULL AND anc3_hemoglobin IS NOT NULL AND anc4_hemoglobin IS NOT NULL;")
				.list();
		assertEquals(2, bpFormsFromDbWithNotNullAnc2_3_4.size());
		List<BpForm> bpFormsFromDbWithNotNullAnc3_4_1 = getCurrentSession()
				.createSQLQuery(
						"SELECT * FROM report.bp_form where anc3_hemoglobin IS NOT NULL AND anc4_hemoglobin IS NOT NULL AND anc1_hemoglobin IS NOT NULL;")
				.list();
		assertEquals(2, bpFormsFromDbWithNotNullAnc3_4_1.size());
		List<BpForm> bpFormsFromDbWithNotNullAnc4_1_2 = getCurrentSession()
				.createSQLQuery(
						"SELECT * FROM report.bp_form where anc4_hemoglobin IS NOT NULL AND anc1_hemoglobin IS NOT NULL AND anc2_hemoglobin IS NOT NULL;")
				.list();
		assertEquals(2, bpFormsFromDbWithNotNullAnc4_1_2.size());
		List<BpForm> bpFormsFromDbWithAtleast1Anc = getCurrentSession()
				.createSQLQuery(
						"SELECT * FROM report.bp_form where anc1_hemoglobin IS NOT NULL OR anc2_hemoglobin IS NOT NULL OR anc3_hemoglobin IS NOT NULL OR anc4_hemoglobin IS NOT NULL")
				.list();
		assertEquals(15, bpFormsFromDbWithAtleast1Anc.size());

	}

	@SuppressWarnings("deprecation")
	@Test
	public void shouldVerifyBpFormValue() {
		List<BpForm> bpFormsToBeAdded = getListOfBpFormsWithAncHemoglobinCombinations();
		addBpFormsToCurrentSession(bpFormsToBeAdded);
		for (int i = 0; i < bpFormsToBeAdded.size(); i++) {
			BpForm bpFormFromDb = (BpForm) getCurrentSession().get(
					BpForm.class, i);
			assertEquals(shouldReturnBpFormWithGivenId(bpFormsToBeAdded, i),
					bpFormFromDb);
		}
	}

	public List<BpForm> getListOfBpFormsWithAncHemoglobinCombinations() {

		List<BpForm> bpFormsToAdd = new ArrayList<BpForm>();
		BpForm bpForm0 = new BpForm();
		bpForm0.setId(0);
		bpFormsToAdd.add(bpForm0);
		BpForm bpForm1 = new BpForm();
		bpForm1.setId(1);
		bpForm1.setAnc1_Hemoglobin(1);
		bpFormsToAdd.add(bpForm1);
		BpForm bpForm2 = new BpForm();
		bpForm2.setId(2);
		bpForm2.setAnc2_Hemoglobin(2);
		bpFormsToAdd.add(bpForm2);
		BpForm bpForm3 = new BpForm();
		bpForm3.setId(3);
		bpForm3.setAnc3_Hemoglobin(3);
		bpFormsToAdd.add(bpForm3);
		BpForm bpForm4 = new BpForm();
		bpForm4.setId(4);
		bpForm4.setAnc4_Hemoglobin(4);
		bpFormsToAdd.add(bpForm4);
		BpForm bpForm5 = new BpForm();
		bpForm5.setId(5);
		bpForm5.setAnc1_Hemoglobin(1);
		bpForm5.setAnc2_Hemoglobin(1);
		bpFormsToAdd.add(bpForm5);
		BpForm bpForm6 = new BpForm();
		bpForm6.setId(6);
		bpForm6.setAnc2_Hemoglobin(2);
		bpForm6.setAnc3_Hemoglobin(1);
		bpFormsToAdd.add(bpForm6);
		BpForm bpForm7 = new BpForm();
		bpForm7.setId(7);
		bpForm7.setAnc3_Hemoglobin(3);
		bpForm7.setAnc4_Hemoglobin(1);
		bpFormsToAdd.add(bpForm7);
		BpForm bpForm8 = new BpForm();
		bpForm8.setId(8);
		bpForm8.setAnc4_Hemoglobin(4);
		bpForm8.setAnc1_Hemoglobin(1);
		bpFormsToAdd.add(bpForm8);
		BpForm bpForm9 = new BpForm();
		bpForm9.setId(9);
		bpForm9.setAnc2_Hemoglobin(1);
		bpForm9.setAnc4_Hemoglobin(1);
		bpFormsToAdd.add(bpForm9);
		BpForm bpForm10 = new BpForm();
		bpForm10.setId(10);
		bpForm10.setAnc3_Hemoglobin(2);
		bpForm10.setAnc1_Hemoglobin(1);
		bpFormsToAdd.add(bpForm10);
		BpForm bpForm11 = new BpForm();
		bpForm11.setId(11);
		bpForm11.setAnc1_Hemoglobin(3);
		bpForm11.setAnc2_Hemoglobin(3);
		bpForm11.setAnc3_Hemoglobin(3);
		bpFormsToAdd.add(bpForm11);
		BpForm bpForm12 = new BpForm();
		bpForm12.setId(12);
		bpForm12.setAnc2_Hemoglobin(4);
		bpForm12.setAnc3_Hemoglobin(4);
		bpForm12.setAnc4_Hemoglobin(4);
		bpFormsToAdd.add(bpForm12);
		BpForm bpForm13 = new BpForm();
		bpForm13.setId(13);
		bpForm13.setAnc3_Hemoglobin(1);
		bpForm13.setAnc4_Hemoglobin(1);
		bpForm13.setAnc1_Hemoglobin(1);
		bpFormsToAdd.add(bpForm13);
		BpForm bpForm14 = new BpForm();
		bpForm14.setId(14);
		bpForm14.setAnc4_Hemoglobin(2);
		bpForm14.setAnc1_Hemoglobin(2);
		bpForm14.setAnc2_Hemoglobin(2);
		bpFormsToAdd.add(bpForm14);
		BpForm bpForm15 = new BpForm();
		bpForm15.setId(15);
		bpForm15.setAnc1_Hemoglobin(3);
		bpForm15.setAnc2_Hemoglobin(3);
		bpForm15.setAnc3_Hemoglobin(3);
		bpForm15.setAnc4_Hemoglobin(3);
		bpFormsToAdd.add(bpForm15);
		return bpFormsToAdd;
	}

	public void addBpFormsToCurrentSession(List<BpForm> bpFormsToBeAdded) {
		for (BpForm bpForm : bpFormsToBeAdded) {
			getCurrentSession().save(bpForm);
		}
	}

	public BpForm shouldReturnBpFormWithGivenId(List<BpForm> bpForms, int id) {
		for (BpForm bpForm : bpForms) {
			if (id == bpForm.getId())
				return bpForm;
		}
		return null;
	}

}
