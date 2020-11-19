package com.greem.project.service;

import com.greem.project.backend.BackEndClient;
import com.greem.project.backend.exceptions.AddChildException;
import com.greem.project.domain.ChildDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChildService {

    @Autowired
    private BackEndClient backEndClient;

    public List<ChildDto> getAllChildren() {
        return backEndClient.getAllChildren();
    }

    public boolean addChild(ChildDto childDto) {
        try {
            backEndClient.addChild(childDto);
        } catch (AddChildException e) {
            return false;
        }
        return true;
    }

}
