package interpreter.command;

import java.util.ArrayList;

public class BlocksCommand extends Command{
    private ArrayList<Command> cmds;

    public BlocksCommand(int line) {
        super(line);
        cmds = new ArrayList<Command>();
    }
   
    public void addCommand(Command statement){
        cmds.add(statement);
    }
    
    @Override
    public void execute() throws Exception{
        for(Command statement : cmds){
            statement.execute();
        }
    }
}