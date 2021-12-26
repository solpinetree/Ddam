package com.ddam.spring.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddam.spring.domain.Crew;
import com.ddam.spring.repository.CrewRepository;

@Service
public class CrewService {
	
//	@Autowired
//    private CrewRepository crewRepository;
//    
//    public CrewService(CrewRepository crewRepository) {
//        this.crewRepository = crewRepository;
//    }
//
//    public int saveCrew(Crew crew) {
//        crewRepository.save(crew);
//        return 1;
//    }
    
//    @Transactional
//    public List<Crew> getCrewList() {
//        List<Crew> crewList = crewRepository.findAll();
//        List<CrewDTO> crewDTOList = new ArrayList<>();
//
//        for(Crew crew : crewList) {
//            CrewDTO crewDTO = CrewDTO.builder()
//    	    		.id(crew.getId())
//    	    		.name(crew.getName())
//    	    		.category(crew.getCategory())
//    	    		.description(crew.getDescription())
//    	    		.mimeType(crew.getMimeType())
//    	    		.area(crew.getArea())
//    	    		.fileName(crew.getFileName())
//    	    		.filePath(crew.getFilePath())
//    	    		.memberLimit(crew.getMemberLimit())
//    	    		.build();
//            crewDTOList.add(crewDTO);
//        }
//        return crewDTOList;
//    }
    
    
//    @Transactional
//    public CrewDTO getCrew(Long id) {
//    	
//    	Crew crew = crewRepository.findById(id).get();
//    
//	    CrewDTO crewDTO = CrewDTO.builder()
//	    		.id(crew.getId())
//	    		.name(crew.getName())
//	    		.category(crew.getCategory())
//	    		.description(crew.getDescription())
//	    		.mimeType(crew.getMimeType())
//	    		.area(crew.getArea())
//	    		.fileName(crew.getFileName())
//	    		.filePath(crew.getFilePath())
//	    		.memberLimit(crew.getMemberLimit())
//	    		.build();
//	    return crewDTO;
//    }
	
}
