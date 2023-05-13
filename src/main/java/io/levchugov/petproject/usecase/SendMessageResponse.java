package io.levchugov.petproject.usecase;

import java.util.List;

public record SendMessageResponse(List<Long> succeed, List<Long> failed) {
}
