package info.source4code.soap.http.cxf;

enum TestHelper {
    ;

    protected static String generateLongName(String name, int size) {
        String result = name;

        for (int i = 0; i < size; i++) {
            result = result + "n";
        }
        return result;
    }
}
