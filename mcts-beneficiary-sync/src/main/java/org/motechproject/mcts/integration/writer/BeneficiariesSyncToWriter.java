package org.motechproject.mcts.integration.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.motechproject.mcts.integration.model.BeneficiaryDetails;
import org.motechproject.mcts.integration.model.BeneficiaryRequest;
import org.motechproject.mcts.integration.service.CareDataService;
import org.motechproject.mcts.integration.service.MCTSHttpClientService;
import org.motechproject.mcts.utils.MctsConstants;
import org.motechproject.mcts.utils.ObjectToXMLConverter;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component("beneficiariesSyncToWriter")
public class BeneficiariesSyncToWriter implements
        ItemWriter<BeneficiaryDetails> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BeneficiariesSyncToWriter.class);

    @Autowired
    private CareDataService careDataService;

    @Autowired
    private MCTSHttpClientService mctsHttpClientService;

    @Autowired
    private PropertyReader propertyReader;

    private String outputXMLFileLocation;

    @Override
    public void write(List<? extends BeneficiaryDetails> beneficiaryDetails)
            throws Exception {
        if (beneficiaryDetails.size() > 0) {
            BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
            beneficiaryRequest
                    .addBeneficiaryDetails((BeneficiaryDetails) beneficiaryDetails);
            HttpStatus httpStatus = syncTo(beneficiaryRequest); // sends Updates
                                                                // to
            // if httpStatus returned from Mcts is 2xx then update the
            // mcts_pregnant_mother_service_updates table
            if (httpStatus.value() / MctsConstants.STATUS_DIVISOR == MctsConstants.STATUS_VALUE) {
                writeSyncDataToFile(beneficiaryRequest); // Writes the updates
                                                         // sent to Mcts to a
                                                         // file
                updateSyncedBeneficiaries(beneficiaryDetails);
            }
        }

    }

    /**
     * Calls the Method from <code>MCTSHttpClientService</code> class to send
     * the updates to Mcts
     *
     * @param beneficiaryRequest
     * @return
     */
    protected HttpStatus syncTo(BeneficiaryRequest beneficiaryRequest) {
        return mctsHttpClientService.syncTo(beneficiaryRequest);
    }

    /**
     * Write the updates to be sent to Mcts in a xml file
     *
     * @param beneficiaryRequest
     * @throws BeneficiaryException
     * @throws URISyntaxException
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    protected void writeSyncDataToFile(BeneficiaryRequest beneficiaryRequest) {

        // To be removed in future and post updates directly to Mcts
        outputXMLFileLocation = String.format("%s_%s.xml", propertyReader
                .getUpdateXmlOutputFileLocation(), DateTime.now().toString(
                "yyyy-MM-dd")
                + "T" + DateTime.now().toString("HH:mm"));
        String outputURLFileLocation = String.format("%s_%s.txt",
                propertyReader.getUpdateUrlOutputFileLocation(), DateTime.now()
                        .toString("yyyy-MM-dd")
                        + "T" + DateTime.now().toString("HH:mm"));
        LOGGER.info("Write Sync Data to File: " + outputXMLFileLocation
                + " & Urls and Headers to file: " + outputURLFileLocation);
        File xmlFile = new File(outputXMLFileLocation);
        File updateRequestUrl = new File(outputURLFileLocation);
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(xmlFile);
            String data = ObjectToXMLConverter.converObjectToXml(
                    beneficiaryRequest, BeneficiaryRequest.class);
            LOGGER.info("Updates Received are:\n" + data);
            printWriter.println(data);
            ObjectToXMLConverter.writeUrlToFile(xmlFile, updateRequestUrl);
        } catch (FileNotFoundException e) {
            throw new BeneficiaryException(ApplicationErrors.FILE_NOT_FOUND, e,
                    e.getMessage());
        } catch (IOException e) {
            throw new BeneficiaryException(
                    ApplicationErrors.FILE_READING_WRTING_FAILED, e, e
                            .getMessage());
        } catch (URISyntaxException e) {
            throw new BeneficiaryException(ApplicationErrors.URI_SYNTAX_ERROR,
                    e, e.getMessage());
        } finally {
            printWriter.close();
        }
    }

    /**
     * Calls <code>updateSyncedBeneficiaries</code> from
     * <code>CareDataService</code> class to update the
     * <code>mcts_pregnant_mother_service_updates</code> table with the updates
     * sent to Mcts
     *
     * @param beneficiaryDetails
     *            List of beneficiaries sent to Mcts
     * @throws BeneficiaryException
     */
    private void updateSyncedBeneficiaries(
            List<? extends BeneficiaryDetails> beneficiaryDetails) {
        LOGGER.info("Updating database with %s Synced Beneficiaries",
                beneficiaryDetails.size());
        List<Beneficiary> syncedBeneficiaries = mapToBeneficiaries((List<BeneficiaryDetails>) beneficiaryDetails);
        careDataService.updateSyncedBeneficiaries(syncedBeneficiaries);
    }

    /**
     * Maps the List of Beneficiaries received from Database to
     * <code>BeneficiaryDetails</code> to be sent to Mcts
     *
     * @param beneficiary
     * @return
     */
    private List<Beneficiary> mapToBeneficiaries(
            List<BeneficiaryDetails> beneficiaryDetails) {
        List<Beneficiary> syncedBeneficiaries = new ArrayList<>();
        for (BeneficiaryDetails beneficiaryDetail : beneficiaryDetails) {
            SimpleDateFormat formatter = new SimpleDateFormat(
                    MctsConstants.DEFAULT_DATE_FORMAT);
            Date serviceDeliveryDate = null;
            try {
                serviceDeliveryDate = formatter.parse(beneficiaryDetail
                        .getAsOnDate());
            } catch (ParseException e) {
                LOGGER.info(
                        "Parse Exception occured while converting date '%s' to java.util.Date",
                        serviceDeliveryDate);
            }
            Beneficiary beneficiary = new Beneficiary(beneficiaryDetail
                    .getMctsId(), beneficiaryDetail.getServiceType(),
                    serviceDeliveryDate);
            syncedBeneficiaries.add(beneficiary);
        }
        return syncedBeneficiaries;
    }

}
