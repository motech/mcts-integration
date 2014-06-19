package org.motechproject.mcts.integration.commcare;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.motechproject.commcare.domain.CaseTask;
import org.motechproject.commcare.domain.IndexTask;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.exception.MalformedCaseXmlException;
import org.motechproject.commcare.request.CaseRequest;
import org.motechproject.commcare.request.CloseElement;
import org.motechproject.commcare.request.CreateElement;
import org.motechproject.commcare.request.IndexSubElement;
import org.motechproject.commcare.request.converter.CloseElementConverter;
import org.motechproject.commcare.request.converter.CreateElementConverter;
import org.motechproject.commcare.request.converter.IndexElementConverter;
import org.motechproject.commcare.request.converter.UpdateElementConverter;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;

@Service
public class CommcareCaseConvertor {

	public String convertToCaseXml(CaseTask task) {
        CaseRequest caseRequest = mapToCase(task);
   
        return convertToXml(caseRequest);
    }
	
	private EventRelay eventRelay;

	@Autowired
	public CommcareCaseConvertor(EventRelay eventRelayOsgi) {
		this.eventRelay = eventRelayOsgi;
	}
	
	
	private CaseRequest mapToCase(CaseTask task) {
        CaseRequest ccCase = createCase(task);

        if (task.getCreateTask() != null) {
            CreateElement create = new CreateElement(task.getCreateTask()
                    .getCaseType(), task.getCreateTask().getCaseName(), task
                    .getCreateTask().getOwnerId());
            ccCase.setCreateElement(create);
        }

        if (task.getUpdateTask() != null) {
            ccCase.setUpdateElement(task.getUpdateTask());
        }

        if (task.getIndexTask() != null) {
            List<IndexSubElement> subElements = task.getIndexTask()
                    .getIndices();
            IndexTask indexElement = new IndexTask(subElements);
            ccCase.setIndexElement(indexElement);
        }

        if (task.getCloseTask() != null) {
            ccCase.setCloseElement(new CloseElement());
        }

        return ccCase;
    }
	
	 private CaseRequest createCase(CaseTask task) {
	        String caseId;

	        if (task.getCreateTask() != null && task.getCaseId() == null) {
	            caseId = UUID.randomUUID().toString();
	        } else {
	            caseId = task.getCaseId();
	        }

	        if (task.getDateModified() == null) {
	            task.setDateModified(DateTime.now().toString());
	        }

	        return new CaseRequest(caseId, task.getUserId(),
	                task.getDateModified(), task.getXmlns());
	    }

	 
	 private String convertToXml(CaseRequest request) {
	        try {

	            XStream xstream = mapEnvelope();

	            if (request.getCreateElement() != null) {
	                xstream.registerConverter(new CreateElementConverter());
	                xstream.aliasField("create", CaseRequest.class, "createElement");
	            } else {
	                xstream.omitField(CaseRequest.class, "createElement");
	            }

	            if (request.getUpdateElement() != null) {
	                xstream.registerConverter(new UpdateElementConverter());
	                xstream.aliasField("update", CaseRequest.class, "updateElement");
	            } else {
	                xstream.omitField(CaseRequest.class, "updateElement");
	            }

	            IndexTask indexElement =request.getIndexElement();

	            if (indexElement != null && indexElement.getIndices().size() > 0) {
	                xstream.registerConverter(new IndexElementConverter());
	                xstream.aliasField("index", CaseRequest.class, "indexElement");
	            } else {
	                xstream.omitField(CaseRequest.class, "indexElement");
	            }

	            if (request.getCloseElement() != null) {
	                xstream.registerConverter(new CloseElementConverter());
	                xstream.aliasField("close", CaseRequest.class, "closeElement");
	            } else {
	                xstream.omitField(CaseRequest.class, "closeElement");
	            }

	            xstream.omitField(CaseRequest.class, "dataXmlns");

	            return xstream.toXML(request);
	        } catch (MalformedCaseXmlException e) {
	            MotechEvent motechEvent = new MotechEvent(
	                    EventSubjects.MALFORMED_CASE_EXCEPTION);
	            motechEvent.getParameters().put(EventDataKeys.MESSAGE,
	                    e.getMessage());
	            eventRelay.sendEventMessage(motechEvent);
	        }

	        return null;
	    }
	 
	 private XStream mapEnvelope() {
	        XStream xstream = new XStream(new DomDriver("UTF-8", new NoNameCoder()));
	        xstream.alias("case", CaseRequest.class);
	        xstream.aliasField("case_id", CaseRequest.class, "caseId");
	        xstream.useAttributeFor(CaseRequest.class, "caseId");
	        xstream.aliasField("case_id", CaseRequest.class, "caseId");
	        xstream.useAttributeFor(CaseRequest.class, "userId");
	        xstream.aliasField("user_id", CaseRequest.class, "userId");
	        xstream.useAttributeFor(CaseRequest.class, "xmlns");
	        xstream.useAttributeFor(CaseRequest.class, "dateModified");
	        xstream.aliasField("date_modified", CaseRequest.class, "dateModified");

	        return xstream;
	    }

}
