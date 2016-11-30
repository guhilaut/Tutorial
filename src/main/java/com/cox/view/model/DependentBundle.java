package com.cox.view.model;

public class DependentBundle {
	
	private String bundleId;
	private String bundleName;

	public String getBundleId() {
		return bundleId;
	}
	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}
	public String getBundleName() {
		return bundleName;
	}
	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bundleId == null) ? 0 : bundleId.hashCode());
		result = prime * result
				+ ((bundleName == null) ? 0 : bundleName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DependentBundle other = (DependentBundle) obj;
		if (bundleId == null) {
			if (other.bundleId != null)
				return false;
		} else if (!bundleId.equals(other.bundleId))
			return false;
		if (bundleName == null) {
			if (other.bundleName != null)
				return false;
		} else if (!bundleName.equals(other.bundleName))
			return false;
		return true;
	}
	
}
