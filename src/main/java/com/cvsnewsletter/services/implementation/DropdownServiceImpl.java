package com.cvsnewsletter.services.implementation;

import com.cvsnewsletter.constants.DropdownConstants;
import com.cvsnewsletter.services.DropdownService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DropdownServiceImpl implements DropdownService {

    @Override
    public Map<String, List<String>> getDropdownValues() {
        return DropdownConstants.getDropdownValues();
    }

}
