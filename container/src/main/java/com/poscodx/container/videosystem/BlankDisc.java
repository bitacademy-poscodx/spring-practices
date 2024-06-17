package com.poscodx.container.videosystem;

public class BlankDisc implements DigitalVideoDisc {
	private String title;
	private String studio;

	public BlankDisc(String title, String studio) {
		this.title = title;
		this.studio = studio;
	}
	
	@Override
	public String play() {
		return "Playing Movie " + studio + "'s " + title;
	}
}
