package src.Command;

import src.File.FileService;
import src.Postgresql.PostgresqlService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommandService {
    FileService fileService;
    public CommandService(String url) {
        this.fileService = new FileService(url);
    }
    private List<JsonNode> convertConfig() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        return objectMapper.readValue(this.fileService.readFile(), typeFactory.constructCollectionType(List.class, JsonNode.class));
    }

    public Object runCommand() throws IOException, SQLException {
        /* Хранения результатов команд */
        Map<String, Object> dataset = new HashMap<>();
        /* Конфиг команд */
        List<JsonNode> config = convertConfig();
        /* Анализ конфига*/
        for (JsonNode element : config) {
            String key = element.get("key").textValue();
            String type = element.get("type").textValue();
            /* проверка что это return */
            if (Objects.equals(type, CommandTypeEnum.returns.getTitle()) ) {
                return dataset.get(key);
            }

            /* Выполнение какой либо команты */
            else {
                Object result = this.checkTypeCommand(element, type);
                dataset.put(key, result);
            }
        }
        return  null;
    }

    private List<Object> runPostgresqlService(JsonNode element) throws SQLException, IOException {
        String sql = String.valueOf(element.get("sql").textValue());
        PostgresqlService postgresqlService = new PostgresqlService();
        List<Object> res = postgresqlService.runSql(sql);
        postgresqlService.closeConn();
        return res;
    }

    private Object checkTypeCommand(JsonNode element, String type) throws SQLException, IOException {
        if (type.equals(CommandTypeEnum.postgresql.getTitle())) {
           return this.runPostgresqlService(element);
        }
        return null;
    }
}
