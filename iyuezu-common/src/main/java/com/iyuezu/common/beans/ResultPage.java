package com.iyuezu.common.beans;

import java.util.List;

public class ResultPage<T> {

	private int page;
	private int row;
	private int total;
	private int pages;
	private List<T> list;
	
	public ResultPage(int page, int row, int total, int pages , List<T> list) {
		this.page = page;
		this.row = row;
		this.total = total;
		this.pages = pages;
		this.list = list;
	}
	
	public ResultPage(int page, int row, int total , List<T> list) {
		this.page = page;
		this.row = row;
		this.total = total;
		this.pages = (int)Math.ceil(((double)total) / row);
		this.list = list;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
