import java.security.PublicKey;
public class UTXO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String hashID;
	private String parentTransactionID;
	private PublicKey reciever;
	private PublicKey sender;
	private long timestamp;
	private double fundTransferred;
	private long sequentialNumber = 0;
	public UTXO(String parentTransactionID, PublicKey sender, PublicKey reciever, double fundToTransfer) {
		this.sequentialNumber = UtilityMethods.getUniqueNumber();
		this.parentTransactionID = parentTransactionID;
		this.reciever = reciever;
		this.sender = sender;
		this.fundTransferred = fundToTransfer;
		this.timestamp = UtilityMethods.getTimeStamp();
		this.hashID = computeHashID();
	}
	protected String computeHashID() {
		String message = this.parentTransactionID
				+ UtilityMethods.getKeyString(this.sender)
				+ UtilityMethods.getKeyString(reciever)
				+ Double.toHexString(this.fundTransferred)
				+ Long.toHexString(this.timestamp)
				+ Long.toHexString(this.sequentialNumber);
		return UtilityMethods.messageDigestSHA256_toString(message);
	}
	public String getHashID() {
		return this.hashID;
	}
	public String getParentTransactionID() {
		return this.parentTransactionID;
	}
	public PublicKey getReciever() {
		return this.reciever;
	}
	public PublicKey getSender() {
		return this.sender;
	}
	public long  getTimeStamp() {
		return this.getTimeStamp();
	}
	public long getSequentialNumber() {
		return this.sequentialNumber;
	}
	public double getFundTransferred() {
		return this.fundTransferred;
	}
	public boolean equals(UTXO uxo) {
		return this.getHashID().equals(uxo.getHashID());
	}
	public boolean isMiningReward() {
		return false;
	}

}
