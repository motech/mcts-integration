package org.motechproject.mcts.integration.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.care.common.mds.model.MctsDistrict;
import org.motechproject.mcts.care.common.mds.model.MctsHealthblock;
import org.motechproject.mcts.care.common.mds.model.MctsHealthworker;
import org.motechproject.mcts.care.common.mds.model.MctsHealthworkerErrorLog;
import org.motechproject.mcts.care.common.mds.model.MctsPhc;
import org.motechproject.mcts.care.common.mds.model.MctsState;
import org.motechproject.mcts.care.common.mds.model.MctsSubcenter;
import org.motechproject.mcts.care.common.mds.model.MctsTaluk;
import org.motechproject.mcts.care.common.mds.model.MctsVillage;
import org.motechproject.mcts.integration.model.FLWDataCSV;
import org.motechproject.mcts.integration.model.Location;
import org.motechproject.mcts.integration.model.LocationDataCSV;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.utils.FlwValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 * Service method to read flw CSV file and populate the database
 *
 * @author aman
 *
 */
@Transactional
@Service
public class FLWDataPopulator {

    private static final  Logger LOGGER = LoggerFactory
            .getLogger(FLWDataPopulator.class);

    public CareDataRepository getCareDataRepository() {
        return careDataRepository;
    }

    public void setCareDataRepository(CareDataRepository careDataRepository) {
        this.careDataRepository = careDataRepository;
    }

    @Autowired
    private CareDataRepository careDataRepository;

    @Autowired
    private LocationDataPopulator locationDataPopulator;

    @Autowired
    private CareDataService careDataService;

    /**
     * Method to populate table mcts_HealthWorker
     *
     * @throws Exception
     */
    public void populateFLWData(MultipartFile file, String stateId) {
        ICsvBeanReader beanReader = null;
        FLWDataCSV flwDataCSV = new FLWDataCSV();
        File newFile = null;
        try {
            byte[] bytes = file.getBytes();
            String path = System.getProperty("java.io.tmpdir");
            newFile = new File(path + "/flw.xml");
            FileOutputStream out = new FileOutputStream(newFile);
            out.write(bytes);
            beanReader = new CsvBeanReader(new FileReader(newFile),
                    CsvPreference.STANDARD_PREFERENCE);
            final String[] header = beanReader.getHeader(true);

            while ((flwDataCSV = beanReader.read(FLWDataCSV.class, header)) != null) {
                if (FlwValidator.isValidateFlw(flwDataCSV)) {
                    addFLWToDb(flwDataCSV, stateId);
                } else {
                    flwDataPopulator(flwDataCSV);
                }
            }

        } catch (FileNotFoundException e) {
            throw new BeneficiaryException(ApplicationErrors.FILE_NOT_FOUND, e);
        } catch (IOException e) {
            throw new BeneficiaryException(
                    ApplicationErrors.FILE_READING_WRTING_FAILED, e);
        } catch (SuperCsvReflectionException e) {
            throw new BeneficiaryException(
                    ApplicationErrors.CSV_FILE_DOES_NOT_MATCH_WITH_HEADERS, e);
        } catch (IllegalArgumentException e) {
            throw new BeneficiaryException(
                    ApplicationErrors.NUMBER_OF_ARGUMENTS_DOES_NOT_MATCH, e);
        } /*catch (HibernateException e) {
            throw new BeneficiaryException(
                    ApplicationErrors.DATABASE_OPERATION_FAILED, e);
        }*/ finally {
            if (beanReader != null) {
                try {
                    beanReader.close();
                } catch (IOException e) {
                    throw new BeneficiaryException(
                            ApplicationErrors.FILE_CLOSING_FAILED, e);
                }
            }

        }

    }

    public void addFLWToDb(FLWDataCSV flwDataCSV, String stateId) {
        LocationDataCSV locationCSV = createLocationCSV(flwDataCSV, stateId);
        locationDataPopulator.addLocationToDb(locationCSV, true);
        Location location = getUniqueLocation(locationCSV);

        MctsPhc mctsPhc = location.getMctsPhc();
        if (mctsPhc != null) {
            int healthworkerId = flwDataCSV.getIdasInteger();
            String name = flwDataCSV.getName();
            String contactNo = flwDataCSV.getContact_No();
            String sex = "";
            if (flwDataCSV.getSex() != null) {
                sex = flwDataCSV.getSex();
            }

            String type = flwDataCSV.getType();
            Integer villageId = flwDataCSV.getVillage_IDasInteger();
            String husbandName = flwDataCSV.getHusband_Name();
            String aadharNo = flwDataCSV.getAadhar_No();
            String gfAddress = flwDataCSV.getGF_Address();

            MctsSubcenter mctsSubcenre = location.getMctsSubcenter();

            MctsHealthworker mctsHealthworker = careDataRepository
                    .findEntityByField(MctsHealthworker.class,
                            "healthworkerId", healthworkerId);
            if (mctsHealthworker == null) {
                mctsHealthworker = new MctsHealthworker();
            }
            mctsHealthworker.setMctsPhc(mctsPhc);
            mctsHealthworker.setHealthworkerId(healthworkerId);
            mctsHealthworker.setName(name);
            mctsHealthworker.setSex(sex);
            mctsHealthworker.setType(type);
            mctsHealthworker.setContactNo(contactNo);
            mctsHealthworker.setHusbandName(husbandName);
            mctsHealthworker.setAadharNo(aadharNo);
            mctsHealthworker.setGfAddress(gfAddress);
            mctsHealthworker.setStatus(true);
            if (mctsSubcenre != null) {
                mctsHealthworker.setMctsSubcenter(mctsSubcenre);
            }

            if (villageId != null) {
                MctsVillage mctsVillage = location.getMctsVillage();

                if (mctsVillage != null) {
                    mctsHealthworker.setMctsVillage(mctsVillage);
                }
            }

            careDataRepository.saveOrUpdate(mctsHealthworker);

        }  else {
            LOGGER.error("invalid phc id in row");
        }

    }

    /**
     * Method to populate table mcts_flw_error
     *
     * @param flwDataCSV
     * @throws BeneficiaryException
     */
    public void flwDataPopulator(FLWDataCSV flwDataCSV) {

        String districtId = flwDataCSV.getDistrict_ID();
        String talukaId = flwDataCSV.getTaluka_ID();
        String healthBlockId = flwDataCSV.getHealthBlock_ID();
        String subCentreId = flwDataCSV.getSubCentre_ID();
        String villageId = flwDataCSV.getVillage_ID();

        String name = flwDataCSV.getName();
        String sex = flwDataCSV.getSex();
        String type = flwDataCSV.getType();
        String aadharNo = flwDataCSV.getAadhar_No();
        String husbandName = flwDataCSV.getHusband_Name();
        String gfAdress = flwDataCSV.getGF_Address();
        String healthWorkerId = flwDataCSV.getId();
        if (healthWorkerId == null) {
            healthWorkerId = "";
        }
        MctsPhc mctsPhc = null;
        String contactNo = flwDataCSV.getContact_No();
        String phcId = flwDataCSV.getPHC_ID();
        if (flwDataCSV.getPHC_IDasInteger() != null) {
        	mctsPhc = (MctsPhc) careDataRepository.findEntityByField(MctsPhc.class, "phcId", flwDataCSV
                    .getPHC_IDasInteger());
        	LOGGER.debug("phc received");
            /*mctsPhc = careDataRepository.getMctsPhc(flwDataCSV
                    .getPHC_IDasInteger());*/
        }

        String comments = "";

        if (mctsPhc == null) {
            comments = "Invalid phc id";
        }
        MctsHealthworkerErrorLog mctsHealthworkerErrorLog = new MctsHealthworkerErrorLog(
                districtId, talukaId, healthBlockId, subCentreId, villageId,
                name, sex, type, aadharNo, husbandName, gfAdress,
                healthWorkerId, contactNo, phcId, comments);
        careDataRepository.saveOrUpdate(mctsHealthworkerErrorLog);
    }

    private LocationDataCSV createLocationCSV(FLWDataCSV flwDataCSV,
            String stateId) {
        LocationDataCSV location = new LocationDataCSV();
        location.setStateID(stateId);
        location.setDCode(flwDataCSV.getDistrict_ID());
        location.setBID(flwDataCSV.getHealthBlock_ID());
        location.setPID(flwDataCSV.getPHC_ID());
        location.setSID(flwDataCSV.getSubCentre_ID());
        location.setVCode(flwDataCSV.getVillage_ID());
        location.setTCode(flwDataCSV.getTaluka_ID());
        location.setVillage("");
        location.setSUBCenter("");
        location.setPHC("");
        location.setTaluka_Name("");
        location.setBlock("");
        location.setState("");
        location.setBlock("");
        location.setDistrict("");
        return location;
    }

    Location getUniqueLocation(LocationDataCSV locationDataCSV) {
        Location location = new Location();
        location.setMctsState(careDataService.findEntityByField(
                MctsState.class, "stateId", IntegerValidator
                        .validateAndReturnAsInt("stateId",
                                locationDataCSV.getStateID())));
        try {
            // sets District
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("mctsState", location.getMctsState());

            params.put("disctrictId", IntegerValidator.validateAndReturnAsInt(
                    "disctrictId", locationDataCSV.getDCode()));
            location.setMctsDistrict(careDataService
                    .findListOfEntitiesByMultipleField(MctsDistrict.class,
                            params).get(0));
            // sets Taluka
            params = new HashMap<String, Object>();
            params.put("mctsDistrict", location.getMctsDistrict());
            params.put("talukId", IntegerValidator.validateAndReturnAsInt(
                    "talukId", locationDataCSV.getTCode()));
            location.setMctsTaluk(careDataService
                    .findListOfEntitiesByMultipleField(MctsTaluk.class, params)
                    .get(0));
            // sets Village
            params = new HashMap<String, Object>();
            params.put("mctsTaluk", location.getMctsTaluk());
            if (locationDataCSV.getVCode() != null
                    && !locationDataCSV.getVCode().isEmpty()) {
                params.put("villageId", IntegerValidator
                        .validateAndReturnAsInt("villageId",
                                locationDataCSV.getVCode()));
                location.setMctsVillage(careDataService
                        .findListOfEntitiesByMultipleField(MctsVillage.class,
                                params).get(0));
            }

            // sets HealthBlock
            params = new HashMap<String, Object>();
            params.put("mctsTaluk", location.getMctsTaluk());
            params.put("healthblockId", IntegerValidator
                    .validateAndReturnAsInt("healthblockId",
                            locationDataCSV.getBID()));
            location.setMctsHealthblock(careDataService
                    .findListOfEntitiesByMultipleField(MctsHealthblock.class,
                            params).get(0));
            // sets Phc
            params = new HashMap<String, Object>();
            params.put("mctsHealthblock", location.getMctsHealthblock());
            params.put("phcId", IntegerValidator.validateAndReturnAsInt(
                    "phcId", locationDataCSV.getPID()));
            location.setMctsPhc(careDataService
                    .findListOfEntitiesByMultipleField(MctsPhc.class, params)
                    .get(0));
            // sets SubCenter
            params = new HashMap<String, Object>();
            params.put("mctsPhc", location.getMctsPhc());
            params.put("subcenterId", IntegerValidator.validateAndReturnAsInt(
                    "subcenterId", locationDataCSV.getSID()));
            location.setMctsSubcenter(careDataService
                    .findListOfEntitiesByMultipleField(MctsSubcenter.class,
                            params).get(0));
        } catch (NumberFormatException e) {
            LOGGER.error(String.format("Invalid Location Code Received"), e);
            throw new BeneficiaryException(ApplicationErrors.NUMBERS_MISMATCH,
                    e);
        }
        return location;
    }
}
