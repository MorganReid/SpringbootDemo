package com.example.demo.strategyDesign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: hujun
 * @date: 2021/04/30  11:47
 */
//建造工厂，根据id找对应的具体类型
@Component
public class SetupTemplateRouter {

    private static final Map<SetupTemplateNatureEnum, SetupTemplate> map = new HashMap<>();
    @Autowired
    private BlockTypeStrategy blockTypeStrategy;
    @Autowired
    private TicketTypeStrategy ticketTypeStrategy;

    @PostConstruct
    public void init() {
        map.put(SetupTemplateNatureEnum.BLOCKTYPE, blockTypeStrategy);
        map.put(SetupTemplateNatureEnum.TICKETTYPE, ticketTypeStrategy);

    }

    public SetupTemplate getInvoker(SetupTemplateNatureEnum setupTemplateNatureEnum) {
        return map.get(setupTemplateNatureEnum);
    }

}
