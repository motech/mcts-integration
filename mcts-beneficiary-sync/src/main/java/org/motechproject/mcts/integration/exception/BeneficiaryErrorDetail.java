package org.motechproject.mcts.integration.exception;

//@JsonSerialize(include = Inclusion.NON_NULL)
public class BeneficiaryErrorDetail {

	private String reason;
    private String stackTrace;
    
 public String getReason() {
  return reason;
 }
 public void setReason(String reason) {
  this.reason = reason;
 }
 public String getStackTrace() {
  return stackTrace;
 }
 public void setStackTrace(String stackTrace) {
  this.stackTrace = stackTrace;
 }

    

}
