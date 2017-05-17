import views.MainView;

public class Launch {
	public static void main(String[] args) throws Exception {

		// Avoid the join error when using on Mac
		System.setProperty("java.net.preferIPv4Stack", "true");


		(new Thread(){
			public void run() {
				MainView.launch((MainView.class));
			}
		}).start();
	}

}
