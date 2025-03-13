package com.edureka.APIAssignment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class product{
	public String title;
	public double price;
	public String description;
	
	@JsonProperty("Image URL")
	public String imageUrl;
	
	public String Category;
}
