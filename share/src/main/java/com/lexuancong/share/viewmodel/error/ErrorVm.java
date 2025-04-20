package com.lexuancong.share.viewmodel.error;

import java.util.List;

public record ErrorVm(int statusCode , String title , String details , List<String> fieldErrors) {

}
