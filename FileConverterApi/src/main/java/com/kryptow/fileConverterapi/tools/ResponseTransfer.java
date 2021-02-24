package com.kryptow.fileConverterapi.tools;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class ResponseTransfer {
	private Boolean status;
    private String message;
    private String url;
    
	public ResponseTransfer(Boolean status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
    
}