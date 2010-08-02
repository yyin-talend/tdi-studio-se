package ionic.Msmq;

  public class Message {
    String _message ;
    String _label ;
    String _correlationId ;
    int _transactionFlag;
    //    int _status; 

    public void setMessage(String value) { _message= value; }
    public String getMessage() { return _message; }

    public void setLabel(String value) { _label= value; }
    public String getLabel() { return _label; }

    public void setCorrelationId(String value) {_correlationId= value;}
    public String getCorrelationId() {return _correlationId; }

    public void setTransactionFlag(int value) { _transactionFlag= value;}
    public int getTransactionFlag() {return _transactionFlag;}

    //    public int getStatus() {return _status;}

    public  Message(String message, String label, String correlationId, int transactionFlag) {
      _message=message;
      _label= label;
      _correlationId= correlationId;
      _transactionFlag= transactionFlag;
    }
//     public Message(String message, String label, String correlationId, int transactionFlag, int status) {
//       _message=message;
//       _label= label;
//       _correlationId= correlationId;
//       _transactionFlag= transactionFlag;
//       _status= status;
//     }
  }
