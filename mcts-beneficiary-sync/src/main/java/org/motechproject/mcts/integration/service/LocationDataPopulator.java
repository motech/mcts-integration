package org.motechproject.mcts.integration.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsDistrict;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthblock;
import org.motechproject.mcts.integration.hibernate.model.MctsLocationErrorLog;
import org.motechproject.mcts.integration.hibernate.model.MctsPhc;
import org.motechproject.mcts.integration.hibernate.model.MctsState;
import org.motechproject.mcts.integration.hibernate.model.MctsSubcenter;
import org.motechproject.mcts.integration.hibernate.model.MctsTaluk;
import org.motechproject.mcts.integration.hibernate.model.MctsVillage;
import org.motechproject.mcts.integration.model.LocationDataCSV;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.utils.LocationValidator;
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

@Transactional
@Service
public class LocationDataPopulator {

    private final static Logger LOGGER = LoggerFactory
            .getLogger(LocationDataPopulator.class);

    public CareDataRepository getCareDataRepository() {
        return careDataRepository;
    }

    public void setCareDataRepository(CareDataRepository careDataRepository) {
        this.careDataRepository = careDataRepository;
    }

    @Autowired
    private CareDataRepository careDataRepository;

    public LocationDataPopulator() {

    }

    /**
     * Method to populate locations such as state, district, taluka, block, phc,
     * subcentre, village
     * 
     * @param file
     * @throws BeneficiaryException
     * @throws IOException
     * @throws FileNotFoundException
     * @throws Exception
     */
    public void populateLocations(MultipartFile file)
            throws BeneficiaryException {
        File newFile = null;
        ICsvBeanReader beanReader = null;
        LocationDataCSV locationCSV = new LocationDataCSV();
        try {
            byte[] bytes = file.getBytes();
            LOGGER.info("thispath" + System.getProperty("java.io.tmpdir"));
            String path = System.getProperty("java.io.tmpdir");
            newFile = new File(path + "/beneficiary.xml");
            FileOutputStream out = new FileOutputStream(newFile);
            out.write(bytes);
            LOGGER.info("size" + newFile.getTotalSpace());
            LOGGER.info("temp path" + newFile.getAbsolutePath());
            beanReader = new CsvBeanReader(new FileReader(newFile),
                    CsvPreference.STANDARD_PREFERENCE);
            final String[] header = beanReader.getHeader(true);
            LOGGER.info("Writing locations to database");
            while ((locationCSV = beanReader
                    .read(LocationDataCSV.class, header)) != null) {
                if (LocationValidator.isValidateLocation(locationCSV) == true) {
                    addLocationToDb(locationCSV, true);
                } else {
                    saveLocationData(locationCSV);
                }
            }
        } catch (FileNotFoundException e) {
            throw new BeneficiaryException(ApplicationErrors.FILE_NOT_FOUND, e,
                    e.getMessage());
        } catch (IOException e) {
            throw new BeneficiaryException(
                    ApplicationErrors.FILE_READING_WRTING_FAILED, e,
                    e.getMessage());
        } catch (SuperCsvReflectionException e) {
            throw new BeneficiaryException(
                    ApplicationErrors.CSV_FILE_DOES_NOT_MATCH_WITH_HEADERS, e,
                    e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BeneficiaryException(
                    ApplicationErrors.NUMBER_OF_ARGUMENTS_DOES_NOT_MATCH, e,
                    e.getMessage());
        }

        finally {
            if (beanReader != null) {
                try {
                    beanReader.close();
                } catch (IOException e) {
                    throw new BeneficiaryException(
                            ApplicationErrors.FILE_CLOSING_FAILED,
                            e.getMessage());
                }
            }
        }

    }

    /**
     * @throws BeneficiaryException
     * 
     */
    public void addLocationToDb(LocationDataCSV locationCSV, boolean status)
            throws BeneficiaryException {
        int stateId = locationCSV.getStateIDasInteger();
        String StateName = locationCSV.getState();
        int disctrictId = locationCSV.getDCodeasInteger();
        String disctrictName = locationCSV.getDistrict();
        int talukId = locationCSV.getTCodeasInteger();
        String talukaName = locationCSV.getTaluka_Name();
        int healthblockId = locationCSV.getBIDasInteger();
        String healthblockName = locationCSV.getBlock();
        int phcId = locationCSV.getPIDasInteger();
        String phcName = locationCSV.getPHC();
        int subcenterId = locationCSV.getSIDasInteger();
        String subcentreName = locationCSV.getSUBCenter();
        Integer villageId = locationCSV.getVCodeasInteger();
        String villageName = locationCSV.getVillage();

        MctsState mctsState = addStateToDbandReturn(stateId, StateName, status);
        MctsDistrict mctsDistrict = findUniqueDistrictandReturn(mctsState,
                disctrictId, disctrictName, status);
        MctsTaluk mctsTaluk = findUniqueTalukandReturn(mctsDistrict, talukId,
                talukaName, status);
        MctsHealthblock mctsHealthblock = findUniqueBlockandReturn(mctsTaluk,
                healthblockId, healthblockName, status);
        MctsPhc mctsPhc = findUniquePhcandReturn(mctsHealthblock, phcId,
                phcName, status);
        MctsSubcenter mctsSubcenter = findUniqueSubcentreandReturn(mctsPhc,
                subcenterId, subcentreName, status);

        if (villageId != null) {
            MctsVillage mctsVillage = careDataRepository.findUniqueVillage(
                    villageId, mctsSubcenter.getId(), mctsTaluk.getId());
            if (mctsVillage == null) {
                mctsVillage = new MctsVillage(mctsTaluk, mctsSubcenter,
                        villageId, villageName);
                mctsVillage.setStatus(false);
            }
            if (!mctsVillage.getStatus()) {
                mctsVillage.setStatus(status);
                careDataRepository.saveOrUpdate(mctsVillage);
            }
        }

    }

    /**
     * Method to populate location master table
     * 
     * @throws BeneficiaryException
     * @throws IOException
     */
    public void saveLocationData(LocationDataCSV locationCSV)
            throws BeneficiaryException {

        String stateId = locationCSV.getStateID();
        String state = locationCSV.getState();

        String disctrictId = locationCSV.getDCode();
        String disctrictName = locationCSV.getDistrict();

        String talukId = locationCSV.getTCode();
        String talukaName = locationCSV.getTaluka_Name();

        String healthblockId = locationCSV.getBID();
        String healthblockName = locationCSV.getBlock();

        String phcId = locationCSV.getPID();
        String phcName = locationCSV.getPHC();

        String subcenterId = locationCSV.getSID();
        String subcentreName = locationCSV.getSUBCenter();

        String villageId = locationCSV.getVCode();
        String villageName = locationCSV.getVillage();
        String status = "1";
        String comments = " ";

        MctsLocationErrorLog mctsLocationMaster = new MctsLocationErrorLog(
                stateId, state, disctrictId, disctrictName, talukId,
                talukaName, healthblockId, healthblockName, phcId, phcName,
                subcenterId, subcentreName, villageId, villageName, status,
                comments);
        careDataRepository.saveOrUpdate(mctsLocationMaster);

    }

    MctsState addStateToDbandReturn(int stateId, String StateName,
            boolean status) throws BeneficiaryException {
        MctsState mctsState = careDataRepository.findEntityByField(
                MctsState.class, "stateId", stateId);
        if (mctsState == null) {
            mctsState = new MctsState(stateId, StateName);
            mctsState.setStatus(false);
        }
        if (!mctsState.getStatus()) {
            mctsState.setStatus(status);
            careDataRepository.saveOrUpdate(mctsState);
        }
        return mctsState;
    }

    private MctsDistrict findUniqueDistrictandReturn(MctsState mctsState,
            int disctrictId, String disctrictName, boolean status)
            throws BeneficiaryException {
        MctsDistrict mctsDistrict = careDataRepository.findUniqueDistrict(
                disctrictId, mctsState.getId());
        if (mctsDistrict == null) {
            mctsDistrict = new MctsDistrict(mctsState, disctrictId,
                    disctrictName);
            mctsDistrict.setStatus(false);
        }
        if (!mctsDistrict.getStatus()) {
            mctsDistrict.setStatus(status);
            careDataRepository.saveOrUpdate(mctsDistrict);
        }
        return mctsDistrict;
    }

    private MctsSubcenter findUniqueSubcentreandReturn(MctsPhc mctsPhc,
            int subcenterId, String subcentreName, boolean status)
            throws BeneficiaryException {
        MctsSubcenter mctsSubcenter = careDataRepository.findUniqueSubcentre(
                subcenterId, mctsPhc.getId());
        if (mctsSubcenter == null) {
            mctsSubcenter = new MctsSubcenter(mctsPhc, subcenterId,
                    subcentreName);
            mctsSubcenter.setStatus(false);
        }
        if (!mctsSubcenter.getStatus()) {
            mctsSubcenter.setStatus(status);
            careDataRepository.saveOrUpdate(mctsSubcenter);
        }
        return mctsSubcenter;
    }

    private MctsPhc findUniquePhcandReturn(MctsHealthblock mctsHealthblock,
            int phcId, String phcName, boolean status)
            throws BeneficiaryException {
        MctsPhc mctsPhc = careDataRepository.findUniquePhc(phcId,
                mctsHealthblock.getId());
        if (mctsPhc == null) {
            mctsPhc = new MctsPhc(mctsHealthblock, phcId, phcName);
            mctsPhc.setStatus(false);
        }
        if (!mctsPhc.getStatus()) {
            mctsPhc.setStatus(status);
            careDataRepository.saveOrUpdate(mctsPhc);
        }
        return mctsPhc;
    }

    private MctsHealthblock findUniqueBlockandReturn(MctsTaluk mctsTaluk,
            int healthblockId, String healthblockName, boolean status)
            throws BeneficiaryException {
        MctsHealthblock mctsHealthblock = careDataRepository
                .findUniqueHealthBlock(healthblockId, mctsTaluk.getId());
        if (mctsHealthblock == null) {
            mctsHealthblock = new MctsHealthblock(mctsTaluk, healthblockId,
                    healthblockName);
            mctsHealthblock.setStatus(false);
        }
        if (!mctsHealthblock.getStatus()) {
            mctsHealthblock.setStatus(status);
            careDataRepository.saveOrUpdate(mctsHealthblock);
        }
        return mctsHealthblock;
    }

    private MctsTaluk findUniqueTalukandReturn(MctsDistrict mctsDistrict,
            int talukId, String talukaName, boolean status)
            throws BeneficiaryException {
        MctsTaluk mctsTaluk = careDataRepository.findUniqueTaluk(talukId,
                mctsDistrict.getId());
        if (mctsTaluk == null) {
            mctsTaluk = new MctsTaluk(mctsDistrict, talukId, talukaName);
            mctsTaluk.setStatus(false);
        }
        if (!mctsTaluk.getStatus()) {
            mctsTaluk.setStatus(status);
            careDataRepository.saveOrUpdate(mctsTaluk);
        }
        return mctsTaluk;
    }

}
