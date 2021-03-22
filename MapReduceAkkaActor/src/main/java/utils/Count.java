package utils;

public class Count
{
    private String word;
    private Integer count;

    public Count(String inWord, Integer inCount) {
        word = inWord;
        count = inCount;
    }

    public String getWord() {
        return word;
    }

    public Integer getCount() {
        return count;
    }
}
