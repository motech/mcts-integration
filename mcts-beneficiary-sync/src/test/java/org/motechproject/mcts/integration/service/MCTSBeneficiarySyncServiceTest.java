package org.motechproject.mcts.integration.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.utils.PropertyReader;
import org.motechproject.mcts.utils.XmlStringToObject;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@PrepareForTest(FileUtils.class)
@RunWith(PowerMockRunner.class)
public class MCTSBeneficiarySyncServiceTest {
   
	@Mock
    private MCTSHttpClientService mctsHttpClientService;

	@Mock
	private Publisher publisher;
	
    @Mock
    private PropertyReader propertyReader;
    
    @Mock
    private XmlStringToObject xmlStringToObject;
    
    @Mock
    private CareDataRepository careDataRepository;
    
	@InjectMocks
	public MCTSBeneficiarySyncService mctsBeneficiarySyncService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldSyncBeneficiaryDataOfMCTSToMotech() throws Exception {
        DateTime startDate = DateTime.now().minusDays(1);
        DateTime endDate = DateTime.now();
        
        MultiValueMap<String, String> requestBody = getRequestBody();
        when(propertyReader.getDefaultBeneficiaryListQueryParams()).thenReturn(getDefaultQueryParams());
        when(mctsHttpClientService.syncFrom(requestBody)).thenReturn(response());

        String response = mctsBeneficiarySyncService.syncFrom(startDate, endDate);

        verify(mctsHttpClientService).syncFrom(requestBody);
        assertEquals(response(),response);
    }
    
    @Test
    public void shouldWriteDataToFile(){
		when(propertyReader.getSyncRequestOutputFileLocation()).thenReturn("updateRequestXML");
		
		mctsBeneficiarySyncService.writeToFile(response());
		verify(propertyReader).getSyncRequestOutputFileLocation();
    }
    
    @Test
    public void shouldNotProceedIfResponseIsNull() throws Exception{
    	DateTime startDate = DateTime.now().minusDays(1);
        DateTime endDate = DateTime.now();
    	MultiValueMap<String, String> requestBody = getRequestBody();
        when(propertyReader.getDefaultBeneficiaryListQueryParams()).thenReturn(getDefaultQueryParams());
    	when(mctsHttpClientService.syncFrom(requestBody)).thenReturn(null);
    	when(xmlStringToObject.stringXmlToObject((Class)any(), (String)any())).thenReturn(null);
    	mctsBeneficiarySyncService.syncBeneficiaryData(startDate, endDate);
    	verify(careDataRepository, times(0)).findEntityByField((Class)any(), (String)any(), (Object)any());
    	verify(publisher, times(0)).publish((String)any(), (String)any());
    	verify(propertyReader, times(0)).getSyncRequestOutputFileLocation();
    }
    
    public MultiValueMap<String, String> getRequestBody(){
    	 DateTime startDate = DateTime.now().minusDays(1);
         DateTime endDate = DateTime.now();
         
         
         MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
         requestBody.putAll(getDefaultQueryParams());
         requestBody.add("FromDate", startDate.toString("dd-MM-yyyy"));
         requestBody.add("ToDate", endDate.toString("dd-MM-yyyy"));
         return requestBody;
    }
    
    public MultiValueMap<String, String> getDefaultQueryParams(){
    	MultiValueMap<String, String> defaultQueryParams = new LinkedMultiValueMap<>();
        defaultQueryParams.add("username", "myUser");
        return defaultQueryParams;
    }

    private String response() {
        return "<newdataset>\n" +
                "    <records>\n" +
                "            <stateid>31</stateid>\n" +
                "            <state_name>Lakshadweep</state_name>\n" +
                "            <district_id>1</district_id>\n" +
                "            <district_name>Lakshadweep</district_name>\n" +
                "            <block_id>1</block_id>\n" +
                "            <block_name>N'Amini'</block_name>\n" +
                "            <tehsil_id>0001</tehsil_id>\n" +
                "            <tehsil_name>AMINI</tehsil_name>\n" +
                "            <facility_id>2</facility_id>\n" +
                "    </records>\n" +
                "</newdataset>";
    }

}
