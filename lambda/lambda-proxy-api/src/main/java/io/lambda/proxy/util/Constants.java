package io.lambda.proxy.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author muditha
 *
 */
public class Constants {	

	public static final int BATCH_SIZE=10;

	public enum ChildRuleType{

		UPGRADE_WITH_BUYBACK("buyBacks"),
		UPGRADE_WITH_DISCOUNT("discounts");

		private static final Map<String, ChildRuleType> childRuleTypeMapping = new HashMap<String, ChildRuleType>();
		static {
			for (ChildRuleType childRuleType : EnumSet.allOf(ChildRuleType.class)) {
				childRuleTypeMapping.put(childRuleType.getChildRuleType(), childRuleType);	    		
			}
		}

		private String childRuleType;

		private ChildRuleType(String childRuleType) {
			this.childRuleType = childRuleType;
		}		

		public String getChildRuleType() {
			return childRuleType;
		}

		public static ChildRuleType getChildRuleType(final String childRuleType) {
			if (childRuleTypeMapping.containsKey(childRuleType)) {
				return childRuleTypeMapping.get(childRuleType);
			} else {
				throw new IllegalArgumentException("Invalid childRuleType , valid child rule types are [buyBacks,discounts]");
			}
		}

	}

}