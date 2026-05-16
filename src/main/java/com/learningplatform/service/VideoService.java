package com.learningplatform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learningplatform.common.BusinessException;
import com.learningplatform.dto.VideoResponse;
import com.learningplatform.dto.VideoWatchRecordRequest;
import com.learningplatform.dto.VideoWatchRecordResponse;
import com.learningplatform.entity.Video;
import com.learningplatform.entity.VideoWatchRecord;
import com.learningplatform.repository.VideoRepository;
import com.learningplatform.repository.VideoWatchRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(value = "transactionManager")
public class VideoService {

    private static final int COMPLETED_PROGRESS_THRESHOLD = 95;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoWatchRecordRepository videoWatchRecordRepository;

    public List<VideoResponse> getVideoList(Long knowledgeId) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        if (knowledgeId != null) {
            queryWrapper.eq("knowledge_point_id", knowledgeId);
        }
        queryWrapper.orderByAsc("sort_order").orderByAsc("id");

        return videoRepository.selectList(queryWrapper).stream()
                .map(this::convertToVideoResponse)
                .collect(Collectors.toList());
    }

    public VideoResponse getVideoDetail(Long videoId) {
        Video video = videoRepository.selectById(videoId);
        if (video == null) {
            throw new BusinessException(1701, "video does not exist");
        }
        return convertToVideoResponse(video);
    }

    public VideoWatchRecordResponse getWatchRecord(Long userId, Long videoId) {
        ensureVideoExists(videoId);

        VideoWatchRecord record = videoWatchRecordRepository.selectOne(buildUserVideoQuery(userId, videoId));
        if (record == null) {
            return buildEmptyWatchRecord(userId, videoId);
        }
        return convertToWatchRecordResponse(record);
    }

    public VideoWatchRecordResponse saveWatchRecord(Long userId, VideoWatchRecordRequest request) {
        ensureVideoExists(request.getVideoId());

        VideoWatchRecord record = videoWatchRecordRepository.selectOne(buildUserVideoQuery(userId, request.getVideoId()));
        if (record == null) {
            record = new VideoWatchRecord();
            record.setUserId(userId);
            record.setVideoId(request.getVideoId());
            fillWatchRecord(record, request);
            videoWatchRecordRepository.insert(record);
            return convertToWatchRecordResponse(record);
        }

        fillWatchRecord(record, request);
        videoWatchRecordRepository.updateById(record);
        return convertToWatchRecordResponse(record);
    }

    private void ensureVideoExists(Long videoId) {
        Video video = videoRepository.selectById(videoId);
        if (video == null) {
            throw new BusinessException(1701, "video does not exist");
        }
    }

    private QueryWrapper<VideoWatchRecord> buildUserVideoQuery(Long userId, Long videoId) {
        return new QueryWrapper<VideoWatchRecord>()
                .eq("user_id", userId)
                .eq("video_id", videoId);
    }

    private void fillWatchRecord(VideoWatchRecord record, VideoWatchRecordRequest request) {
        Integer progress = request.getWatchProgress();
        record.setWatchProgress(progress);
        record.setWatchDuration(request.getWatchDuration() == null ? 0 : request.getWatchDuration());
        record.setLastWatchTime(LocalDateTime.now());
        record.setIsCompleted(progress >= COMPLETED_PROGRESS_THRESHOLD ? 1 : 0);
    }

    private VideoWatchRecordResponse buildEmptyWatchRecord(Long userId, Long videoId) {
        VideoWatchRecordResponse response = new VideoWatchRecordResponse();
        response.setUserId(userId);
        response.setVideoId(videoId);
        response.setWatchProgress(0);
        response.setWatchDuration(0);
        response.setIsCompleted(0);
        return response;
    }

    private VideoResponse convertToVideoResponse(Video video) {
        VideoResponse response = new VideoResponse();
        BeanUtils.copyProperties(video, response);
        return response;
    }

    private VideoWatchRecordResponse convertToWatchRecordResponse(VideoWatchRecord record) {
        VideoWatchRecordResponse response = new VideoWatchRecordResponse();
        BeanUtils.copyProperties(record, response);
        return response;
    }
}
