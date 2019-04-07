package com.nodedynamics.userservices.common;

public class Global {
	
	public static enum ImageType
	{
		PROFILE("PROFILE"),
		EVENT("EVENT");
		
		public final String key;
		private ImageType(final String key) {
			this.key=key;
		}
	}
	
	public static enum VAR
	{
		SESSION("Session");
		
		public final String key;
		private VAR(final String key) {
			this.key=key;
		}
	}
	
	public static enum MessageType
	{
		SUCCESS("Success"),
		ERROR("Error");
		public final String key;
		private MessageType(final String key)
		{
			this.key=key;
		}
	}
	
	public static enum MessageTypeID
	{
		SUCCESS(1),
		ERROR(0);
		
		public final int key;
		private MessageTypeID(final int key) {
			this.key=key;
		}
	}
	
	public static enum UserType
	{
		ENDUSER(1),
		VENDOR(2),
		ALL(3),
		DEVICE(4);
		
		public final int key;
		private UserType(final int key)
		{
			this.key=key;
		}
	}
	
	public static enum UserState
	{
		INACTIVE(0),
		ACTIVE(1),
		PENDING(2);
		
		public final int key;
		private UserState(final int key)
		{
			this.key=key;
		}
	}
	
	//DREAM OBJECT
	public static enum STORAGE
	{
		URL("objects-us-west-1.dream.io"),
		ACCESSKEY("0DeuciVABS4NFUSl_kPJ"),
		SECRETKEY("VN9q0zMjg3gslkxQeZLJ_xDoFimZsoDde7h8k943"),
		FOLDER("eventixz");
		
		public final String key;
		
		private STORAGE(final String key)
		{
			this.key=key;
		}
	}

}
