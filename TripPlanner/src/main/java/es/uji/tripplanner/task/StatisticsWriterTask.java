package es.uji.tripplanner.task;

import java.util.concurrent.BlockingQueue;

import es.uji.geotec.commonutils.Log;
import es.uji.geotec.jerseyutils.dao.StatisticsInfoDAO;
import es.uji.geotec.jerseyutils.model.StatisticInfo;
import es.uji.geotec.scheduledexecutorframework.task.AbstractScheduledTask;

public class StatisticsWriterTask extends AbstractScheduledTask {

	private static final String TAG = StatisticsWriterTask.class.getName();
	
	private static final String NAME = "statistics";
	private BlockingQueue<StatisticInfo> queue;
	
	public StatisticsWriterTask(final BlockingQueue<StatisticInfo> queue) {
		super(NAME, true); // Is a service
		this.queue = queue;
	}

	@Override
	protected void doJob() {
		
		final StatisticsInfoDAO dao = new StatisticsInfoDAO();
		dao.link();
		
		int cont = 0;
		StatisticInfo element = queue.poll();
		while( element != null ) {
			dao.insert(element);
			element = queue.poll();
			cont ++;
		}
		
		dao.unlink();
		
		if( cont != 0 )
			Log.i(TAG, cont + " statistic info has been written");
	}
	
}
