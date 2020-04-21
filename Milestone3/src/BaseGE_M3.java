public abstract class BaseGE_M3  extends Thread{
		protected static boolean isRunning = false;
		public BaseGE_M3() {
			Awake();
			BaseGE_M3.isRunning = true;
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

