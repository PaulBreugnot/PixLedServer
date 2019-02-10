package com.pixled.pixledserver.model.device.base.dto;

import com.pixled.pixledserver.core.color.ColorDto;
import com.pixled.pixledserver.core.device.base.DeviceDto;
import com.pixled.pixledserver.model.color.dao.ColorDao;
import com.pixled.pixledserver.core.device.base.Device;
import com.pixled.pixledserver.model.device.base.dao.DeviceDao;
import com.pixled.pixledserver.model.group.dao.DeviceGroupDao;
import com.pixled.pixledserver.mqtt.MqttConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/devices")
@Transactional
public class DeviceController {

    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private ColorDao colorDao;
    @Autowired
    private DeviceGroupDao deviceGroupDao;
    @Autowired
    private MqttConnection mqttConnection;


    @GetMapping
    public List<DeviceDto> findAll() {
        return deviceDao.findAll()
                .stream()
                .map(device -> device.generateDto())
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public DeviceDto findById(@PathVariable Integer id) {
        return deviceDao.findById(id).map(device -> device.generateDto()).orElse(null);
    }

    @PutMapping(path = "/{id}/switch")
    public DeviceDto switchDevice(@PathVariable Integer id) {
        Device device = deviceDao.findById(id).orElseThrow(IllegalArgumentException::new);
        device.switchDevice();
        deviceDao.save(device);
        // deviceGroupDao.save(light.getRoom());
//        mqttConnection.publishSwitch(
//                light.getRoom().getBuilding().getId(),
//                light.getRoom().getId(),
//                light.getId(),
//                light.getStatus());
        return device.generateDto();
    }
//
    @PutMapping(path = "/{id}/color")
    public DeviceDto changeColor(@PathVariable Integer id, @RequestBody ColorDto color) {
        Device device = deviceDao.findById(id).orElseThrow(IllegalArgumentException::new);
        device.getDeviceState().getColor().setHue(color.getHue());
        device.getDeviceState().getColor().setSaturation(color.getSaturation());
        device.getDeviceState().getColor().setValue(color.getValue());
        colorDao.save(device.getDeviceState().getColor());
//        mqttConnection.publishColor(
//                light.getRoom().getBuilding().getId(),
//                light.getRoom().getId(),
//                light.getId(),
//                color.getArgb().toString());

        return device.generateDto();
    }
//
//    @PostMapping
//    public DeviceDto create(@RequestBody DeviceDto dto) {
//        Device light = null;
//        if (dto.getId() != null) {
//            light = lightDao.findById(dto.getId()).orElse(null);
//        }
//
//        if (light == null) {
//            Device newLight = new Device(
//                    dto.getLevel() == null? 0 : dto.getLevel(),
//                    dto.getStatus() == null? ToggleState.OFF : dto.getStatus(),
//                    dto.getRoomId() == null? null : roomDao.getOne(dto.getRoomId()));
//
//            if (newLight.getColor() == null){
//                colorDao.save(new Color(0F, 0F, 1F, -1));
//            }
//            else {
//                colorDao.save(newLight.getColor());
//            }
//            light = lightDao.save(newLight);
//        } else {
//            if (dto.getLevel() != null) {
//                light.setLevel(dto.getLevel());
//            }
//            if (dto.getStatus() != null) {
//                light.setStatus(dto.getStatus());
//            }
//            lightDao.save(light);
//        }
//
//        return new DeviceDto(light);
//    }
//
//    @DeleteMapping(path = "/{id}")
//    public void delete(@PathVariable Long id) {
//        lightDao.deleteById(id);
//    }
}
