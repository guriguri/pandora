package pandora.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@SuppressWarnings("serial")
abstract public class BaseDomain implements Serializable {
	public String toString(ToStringStyle style) {
		return ToStringBuilder.reflectionToString(this, style);
	}

	@Override
	public String toString() {
		return toString(ToStringStyle.MULTI_LINE_STYLE);
	}
}
