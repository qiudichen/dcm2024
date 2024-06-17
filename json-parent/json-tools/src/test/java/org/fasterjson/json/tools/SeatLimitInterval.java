package org.fasterjson.json.tools;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

public class SeatLimitInterval implements Serializable {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Optional<Integer> minimumSeatLimit;
    private Optional<Integer> maximumSeatLimit;
    
    public SeatLimitInterval() {
    	
    }

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	public Optional<Integer> getMinimumSeatLimit() {
		return minimumSeatLimit;
	}

	public void setMinimumSeatLimit(Optional<Integer> minimumSeatLimit) {
		this.minimumSeatLimit = minimumSeatLimit;
	}

	public Optional<Integer> getMaximumSeatLimit() {
		return maximumSeatLimit;
	}

	public void setMaximumSeatLimit(Optional<Integer> maximumSeatLimit) {
		this.maximumSeatLimit = maximumSeatLimit;
	}
    
}
