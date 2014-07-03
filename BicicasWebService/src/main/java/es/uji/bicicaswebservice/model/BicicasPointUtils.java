package es.uji.bicicaswebservice.model;

import java.util.ArrayList;
import java.util.List;

import es.uji.bicicasdatamodel.BicicasPoint;
import es.uji.bicicasdatamodel.BicicasPointRecord;
import es.uji.bicicasdatamodel.Dock;
import es.uji.bicicaswebservice.messages.ErrorMessages;
import es.uji.geotec.commonutils.DateUtils;

/**
 * Utils methos for the POJO objects that refers to bicicas points
 *
 */
public class BicicasPointUtils {

	/**
	 * Check if the list of bikes is equals.
	 * 
	 * @param before before dock status
	 * @param after after dock status
	 * @return if are equals or not
	 */
	public static boolean hasDockListStatusChanged(final BicicasPoint before, final BicicasPoint after) {
		if( before == null || after == null )
			return true;
		if( before.currentState == null || after.currentState == null )
			return true;
		
		return ! before.currentState.docks.equals(after.currentState.docks);
	}
	
	/**
	 * Given two status, get the ingoing bikes. That is, the bikes that has changed in the new status
	 * If before is null, the result is the after dock list. This is because the dock status of the update is considered as ingoing bike
	 * 
	 * @param before before dock status
	 * @param after after dock status
	 * @return the list of ingoing bikes code
	 */
	public static List<Dock> getIngoingBikes(final BicicasPoint before, final BicicasPoint after) {

		final List<Dock> ingoing = new ArrayList<>();
		
		// This is the case of the first iteration
		if( before == null || before.currentState == null || before.currentState.docks == null ) {
			
//			for( final Dock dock : after.currentState.docks )
//				if( ! dock.bikeCode.equals(Dock.EMPTY_DOCK) )
//					ingoing.add(dock);
			// In the first iteration, the ingoing bikes are empty
			return ingoing;
		}
		
		if( before.currentState.docks.size() != after.currentState.docks.size() )
			throw new IllegalArgumentException(ErrorMessages.DOCKS_LIST_MUST_HAVE_SAME_SIZE);
		
		for(int i = 0; i < before.currentState.docks.size(); i++ ) 
			// if after != before and after is not empty
			if( ! before.currentState.docks.get(i).equals(after.currentState.docks.get(i)) && ! after.currentState.docks.get(i).bikeCode.equals(Dock.EMPTY_DOCK) )
				ingoing.add(after.currentState.docks.get(i));
		
		return ingoing;
	}
	
	/**
	 * Given two status, get the outgoing bikes. That is, the bikes that has left the bicicaspoint.
	 * If before is null, the result is an empty list. This is because no bike has left
	 * 
	 * @param before before dock status
	 * @param after after dock status
	 * @return the list of outgoing bikes code
	 */
	public static List<Dock> getOutgoingBikes(final BicicasPoint before, final BicicasPoint after) {
		
		// This is the case of the first iteration
		if( before == null || before.currentState == null || before.currentState.docks == null ) 
			return new ArrayList<>();
		
		if( before.currentState.docks.size() != after.currentState.docks.size() )
			throw new IllegalArgumentException(ErrorMessages.DOCKS_LIST_MUST_HAVE_SAME_SIZE);
		
		final List<Dock> outgoing = new ArrayList<>();
		
		for(int i = 0; i < before.currentState.docks.size(); i++ ) 
			// if after != before and before is not empty
			if( ! before.currentState.docks.get(i).equals(after.currentState.docks.get(i)) && ! before.currentState.docks.get(i).bikeCode.equals(Dock.EMPTY_DOCK) )
				outgoing.add(before.currentState.docks.get(i));
		
		return outgoing;
	}
	
	/**
	 * Build and get a record given the two parameters. The record represents the changes between the two bicicas point parameters.
	 * 
	 * @param before before dock status
	 * @param after after dock status
	 * @return the record that stores the differences between before and after
	 */
	public static BicicasPointRecord buildRecord(final BicicasPoint before, final BicicasPoint after) {
		final BicicasPointRecord record = new BicicasPointRecord();
		record.bicicasPointCode = after.code;
		record.state = after.currentState;
		record.state.updated = DateUtils.nowDateES();
		record.state.ingoing = getIngoingBikes(before, after);
		record.state.outgoing = getOutgoingBikes(before, after);
		return record;
	}

}
