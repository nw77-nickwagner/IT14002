	import java.io.Serializable;
	public class PayloadM2 implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -6625037986217386003L;
		private String message;
		public void setMessage(String s) {
			this.message = s;
		}
		public String getMessage() {
			return this.message;
		}
		
		private PayloadTypeM2 payloadType;
		public void setPayloadType(PayloadTypeM2 pt) {
			this.payloadType = pt;
		}
		public PayloadTypeM2 getPayloadType() {
			return this.payloadType;
		}
		
		private int number;
		public void setNumber(int n) {
			this.number = n;
		}
		public int getNumber() {
			return this.number;
		}
		
		private String username;
		public void setUsername(String username) {
			this.username = username;
		}
		
		public String getUsername(){
			return this.username;
		}
		
		private boolean isDealt, isShuffled;
		public void setDealt(boolean isDealt) {
			this.isDealt = isDealt;
		}
		
		public void setShuffled(boolean isShuffled) {
			this.isShuffled = isShuffled;
		}
		
		public boolean getDealt() {
			return this.isDealt;
		}
		
		public boolean getShuffled() {
			return this.isShuffled;
		}
		@Override
		public String toString() {
			return String.format("Type[%s], Number[%s], Message[%s], Username[%s], Dealt[%s], Shuffled[%s]",
						getPayloadType().toString(), getNumber(), getMessage(), getUsername(), getDealt(), getShuffled());
		}
	}

