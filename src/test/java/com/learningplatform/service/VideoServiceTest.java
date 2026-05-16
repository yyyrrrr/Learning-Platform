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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private VideoWatchRecordRepository videoWatchRecordRepository;

    @InjectMocks
    private VideoService videoService;

    @Test
    void shouldQueryVideoListOrderedBySortOrder() {
        Video video = new Video();
        video.setId(2L);
        video.setKnowledgePointId(10L);
        video.setTitle("Stack overview");
        when(videoRepository.selectList(any())).thenReturn(List.of(video));

        List<VideoResponse> responses = videoService.getVideoList(10L);

        assertEquals(1, responses.size());
        assertEquals("Stack overview", responses.get(0).getTitle());

        ArgumentCaptor<QueryWrapper<Video>> captor = ArgumentCaptor.forClass(QueryWrapper.class);
        verify(videoRepository).selectList(captor.capture());
        String sqlSegment = captor.getValue().getSqlSegment();
        assertTrue(sqlSegment.contains("knowledge_point_id"));
        assertTrue(sqlSegment.contains("ORDER BY sort_order ASC,id ASC"));
    }

    @Test
    void shouldThrowWhenVideoDoesNotExist() {
        when(videoRepository.selectById(99L)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> videoService.getVideoDetail(99L));

        assertEquals(1701, exception.getCode());
        assertEquals("video does not exist", exception.getMessage());
    }

    @Test
    void shouldInsertFirstWatchRecord() {
        when(videoRepository.selectById(1L)).thenReturn(existingVideo());
        when(videoWatchRecordRepository.selectOne(any())).thenReturn(null);

        VideoWatchRecordRequest request = watchRequest(1L, 40, 120);
        VideoWatchRecordResponse response = videoService.saveWatchRecord(7L, request);

        assertEquals(7L, response.getUserId());
        assertEquals(1L, response.getVideoId());
        assertEquals(40, response.getWatchProgress());
        assertEquals(0, response.getIsCompleted());
        verify(videoWatchRecordRepository).insert(any(VideoWatchRecord.class));
        verify(videoWatchRecordRepository, never()).updateById(any(VideoWatchRecord.class));
    }

    @Test
    void shouldUpdateExistingWatchRecord() {
        when(videoRepository.selectById(1L)).thenReturn(existingVideo());
        VideoWatchRecord existingRecord = new VideoWatchRecord();
        existingRecord.setId(3L);
        existingRecord.setUserId(7L);
        existingRecord.setVideoId(1L);
        existingRecord.setWatchProgress(20);
        existingRecord.setWatchDuration(50);
        existingRecord.setIsCompleted(0);
        when(videoWatchRecordRepository.selectOne(any())).thenReturn(existingRecord);

        VideoWatchRecordResponse response = videoService.saveWatchRecord(7L, watchRequest(1L, 80, 300));

        assertEquals(3L, response.getId());
        assertEquals(80, response.getWatchProgress());
        assertEquals(300, response.getWatchDuration());
        verify(videoWatchRecordRepository).updateById(existingRecord);
        verify(videoWatchRecordRepository, never()).insert(any(VideoWatchRecord.class));
    }

    @Test
    void shouldMarkCompletedWhenProgressAtLeast95() {
        when(videoRepository.selectById(1L)).thenReturn(existingVideo());
        when(videoWatchRecordRepository.selectOne(any())).thenReturn(null);

        VideoWatchRecordResponse response = videoService.saveWatchRecord(7L, watchRequest(1L, 95, 600));

        assertEquals(95, response.getWatchProgress());
        assertEquals(1, response.getIsCompleted());
    }

    private Video existingVideo() {
        Video video = new Video();
        video.setId(1L);
        video.setTitle("Linked list");
        return video;
    }

    private VideoWatchRecordRequest watchRequest(Long videoId, Integer progress, Integer duration) {
        VideoWatchRecordRequest request = new VideoWatchRecordRequest();
        request.setVideoId(videoId);
        request.setWatchProgress(progress);
        request.setWatchDuration(duration);
        return request;
    }
}
