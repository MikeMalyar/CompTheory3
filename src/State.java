public class State {

    private int name;
    private boolean isFinal;
    private String output;

    public int getName() {
        return name;
    }

    public State setName(int name) {
        this.name = name;
        return this;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public State setFinal(boolean aFinal) {
        isFinal = aFinal;
        return this;
    }

    public String getOutput(String res) {
        return output != null ? String.format(output, res) + "\n" : "";
    }

    public State setOutput(String output) {
        this.output = output;
        return this;
    }
}
