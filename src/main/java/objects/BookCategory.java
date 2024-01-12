package objects;

public class BookCategory {

    private Integer code;
    private String description;

    public BookCategory() {
    }

    public BookCategory(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    private void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BookCategory{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        BookCategory category = (BookCategory) obj;
        return this.code.equals(category.getCode());
    }
}
