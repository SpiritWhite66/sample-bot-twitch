package io.bot.personnal.application.service.impl;

import io.bot.personnal.application.service.ModeratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ModeratorServiceImpl implements ModeratorService {

    private final Logger log = LoggerFactory.getLogger(ModeratorServiceImpl.class);

}
