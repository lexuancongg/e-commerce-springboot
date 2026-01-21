package com.lexuancong.share.dto.error;

import java.util.List;

public record ErrorResponse(int statusCode , String title , String details , List<String> fieldErrors) {

}
