package com.lexuancong.oder.service;

import com.lexuancong.oder.repository.OderRepository;
import com.lexuancong.oder.viewmodel.OrderPostVm;
import com.lexuancong.oder.viewmodel.OrderVm;
import org.springframework.stereotype.Service;

@Service
public class OderService {
    private final OderRepository oderRepository;
    // tự thêm bean cần thieeys vào
    public OderService(OderRepository oderRepository) {
        this.oderRepository = oderRepository;
    }
    public OrderVm createOrder(OrderPostVm orderPostVm){

    }

}
