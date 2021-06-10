package com.xoxo.logistic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="logistics")
@Component
public class TransportConfigProperties {

		private PricePercent expectedPrice = new PricePercent();
		private boolean test;

		public static class PricePercent {

		private int lessThan30minutes;
		private int lessThan60minutes;
		private int lessThan120minutes;
		private int moreThan120minutes;

		
		public int getLessThan30minutes() {
			return lessThan30minutes;
		}

		public void setLessThan30minutes(int lessThan30minutes) {
			this.lessThan30minutes = lessThan30minutes;
		}

		public int getLessThan60minutes() {
			return lessThan60minutes;
		}

		public void setLessThan60minutes(int lessThan60minutes) {
			this.lessThan60minutes = lessThan60minutes;
		}

		public int getLessThan120minutes() {
			return lessThan120minutes;
		}

		public void setLessThan120minutes(int lessThan120minutes) {
			this.lessThan120minutes = lessThan120minutes;
		}

		public int getMoreThan120minutes() {
			return moreThan120minutes;
		}

		public void setMoreThan120minutes(int moreThan120minutes) {
			this.moreThan120minutes = moreThan120minutes;
		}
		}
	
		public PricePercent getPricePercent() {
			return expectedPrice;
		}
	
		public void setPricePercent(PricePercent expectedPrice) {
			this.expectedPrice = expectedPrice;
		}
		
		public boolean isTest() {
			return test;
		}
	
		public void setTest(boolean test) {
			this.test = test;
		}
}
