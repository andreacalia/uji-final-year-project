package es.uji.bicicasdatamodel;

public class Dock {
	public static final Integer EMPTY_DOCK = -1;
	
	public Integer bikeCode;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dock other = (Dock) obj;
		if (bikeCode == null) {
			if (other.bikeCode != null)
				return false;
		} else if (!bikeCode.equals(other.bikeCode))
			return false;
		return true;
	}

	public Dock(Integer bikeCode) {
		super();
		this.bikeCode = bikeCode;
	}

	public Dock() {
		super();
	}
}