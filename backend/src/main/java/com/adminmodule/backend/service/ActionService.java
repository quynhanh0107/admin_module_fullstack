package com.adminmodule.backend.service;

import com.adminmodule.backend.entity.Action;
import com.adminmodule.backend.repository.ActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ActionService {
    private final ActionRepository actionRepository;

    // tạo 1 quyền mới
    // vd: code = "CREATE_USER", module = "USER_MANAGEMENT"
    public Action createAction(String actionCode, String module) {
        Optional<Action> existingAction = actionRepository.findByCode(actionCode);
        if (existingAction.isPresent()) {
            throw new RuntimeException("Mã quyền này đã tồn tại!");
        }

        Action newAction = new Action();
        newAction.setCode(actionCode);
        newAction.setModule(module);

        return actionRepository.save(newAction);
    }
    
    // lấy danh sách toàn bộ quyền hiện có
    public List<Action> getActions() {
        return actionRepository.findAll();
    }
}
