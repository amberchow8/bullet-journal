package cs3500.pa05.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a theme json.
 *
 * @param name the name of the theme
 */
public record ThemeJson(
    @JsonProperty("name") String name
) {


}
