import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
public class Transaction implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	public static final double TRANSACTION_FEE = 1.0;
	private String hashID;
	private PublicKey sender;
	private PublicKey[] recievers;
	private double[] fundToTransfer;
	private long timestamp;
	private ArrayList<UTXO> inputs = null;
	private ArrayList<UTXO> outputs = new ArrayList<UTXO>(4);
	private byte[] signature = null;
	private boolean signed = false;
	private long mySequentialNumber;
	public Transaction(PublicKey sender, PublicKey reciever, double fundToTransfer, ArrayList<UTXO> inputs) {
		PublicKey[] pks = new PublicKey[1];
		pks[0] = reciever;
		double[] funds = new double[1];
		funds[0] = fundToTransfer;
		this.setUp(sender, pks, funds, inputs);
	}
	public Transaction(PublicKey sender, PublicKey[] recievers, double[] fundToTransfer, ArrayList<UTXO> inputs) {
		this.setUp(sender, recievers, fundToTransfer, inputs);
	}
	private void setUp(PublicKey sender, PublicKey[] recievers, double[] fundToTransfer, ArrayList<UTXO> inputs) {
		this.mySequentialNumber = UtilityMethods.getUniqueNumber();
		this.sender = sender;
		this.recievers = new PublicKey[1];
		this.recievers = recievers;
		this.fundToTransfer = fundToTransfer;
		this.inputs = inputs;
		this.timestamp = java.util.Calendar.getInstance().getTimeInMillis();
		computeHashID();
	}
	public void signTheTransaction(PrivateKey privateKey) {
		if(this.signature == null && !signed) {
			this.signature = UtilityMethods.generateSignature(privateKey, getMessageData());
			signed = true;
		}
	}
	public boolean verifySignature() {
		String message = getMessageData();
		return UtilityMethods.verifySignature(this.sender, this.signature, message);
	}
	private String getMessageData() {
		StringBuilder sb = new StringBuilder();
		sb.append(UtilityMethods.getKeyString(sender)
				+Long.toHexString(this.timestamp)
				+Long.toString(this.mySequentialNumber));
		for(int i=0; i<this.recievers.length; i++) {
			sb.append(UtilityMethods.getKeyString(this.recievers[i])
					+ Double.toHexString(this.fundToTransfer[i]));
		}
		for(int i=0; i<this.getNumberOfInputUTXOs(); i++) {
			UTXO ut = this.getInputUTXO(i);
			sb.append(ut.getHashID());
		}
		return sb.toString();
	}
	protected void computeHashID() {
		String message = getMessageData();
		this.hashID = UtilityMethods.messageDigestSHA256_toString(message);
	}
	public String getHashID(){
		return this.hashID;
	}
	public PublicKey getSender() {
		return this.sender;
	}
	public long getTimeStamp() {
		return this.timestamp;
	}
	public long getSequentialNumber() {
		return this.mySequentialNumber;
	}
	public double getTotalFundToTransfer() {
		double f = 0;
		for(int i=0; i<this.fundToTransfer.length; i++) {
			f += this.fundToTransfer[i];
		}
		return f;
	}
	protected void addOutputUTXO(UTXO ut) {
		if(!signed) {
			outputs.add(ut);
		}
	}
	public int getNumberOfOutputUTXOs() {
		return this.outputs.size();
	}
	public UTXO getOutputUTXO(int i) {
		return this.outputs.get(i);
	}
	public int getNumberOfInputUTXOs() {
		if(this.inputs == null) {
			return 0;
		}
		return this.inputs.size();
	}
	public UTXO getInputUTXO(int i) {
		return this.inputs.get(i);
	}
	public boolean equals(Transaction T) {
		return this.getHashID().equals(T.getHashID());
	}
}
