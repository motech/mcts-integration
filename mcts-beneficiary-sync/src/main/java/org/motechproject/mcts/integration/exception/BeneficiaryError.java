package org.motechproject.mcts.integration.exception;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Error")
// @JsonSerialize(include = Inclusion.NON_NULL)
public class BeneficiaryError {

    // transactionId
    private String errorCode;
    private String errorMessage;
    private String application;
    private Date timeStamp;
    private String hostName;

    // BatchErrorDetail

    public BeneficiaryError() {
        timeStamp = new Date();
        hostName = getNetworkHostName();
    }

    private String getNetworkHostName() {
        String name = null;

        try {
            name = InetAddress.getLocalHost().getCanonicalHostName();

            if ("localhost".equalsIgnoreCase(name)) {
                NetworkInterface networkInterface = NetworkInterface
                        .getByName("eth0");

                Enumeration<InetAddress> a = networkInterface
                        .getInetAddresses();

                for (; a.hasMoreElements();) {
                    InetAddress addr = a.nextElement();
                    name = addr.getCanonicalHostName();
                    // Check for ipv4 only
                    if (!name.contains(":")) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new BeneficiaryException(
                    ApplicationErrors.FILE_READING_WRTING_FAILED, e,
                    e.getMessage());
        }

        return name;
    }

    // //@XmlJavaTypeAdapter(JaxbRFC3339DateSerializer.class)
    // @JsonSerialize(using=JsonRFC3339DateSerializer.class)
    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

}
