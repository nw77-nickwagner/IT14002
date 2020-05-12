public abstract class BaseGE_Final  extends Thread{
		protected static boolean isRunning = false;
		public BaseGE_Final() {
			Awake();
			BaseGE_Final.isRunning = true;
		}

		protected abstract void Awake();

		protected abstract void OnStart();

		@Override
		public void run() {
			OnStart();
			while(isRunning) {
				Update();
				UILoop();
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			End();
		}

		protected abstract void Update();
		protected abstract void UILoop();

		protected abstract void End();
}

