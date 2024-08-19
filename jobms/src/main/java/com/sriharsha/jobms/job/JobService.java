package com.sriharsha.jobms.job;

import com.sriharsha.jobms.job.dto.JobDTO;

import java.util.List;

public interface JobService {

    List<JobDTO> findAll();
    void createJob(Job job);
    JobDTO getJobById(Long id);

    boolean deleteJobById(Long id);

    boolean updateJob(long id, Job updatedJob);
}
