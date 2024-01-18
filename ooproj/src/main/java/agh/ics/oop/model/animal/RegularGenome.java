package agh.ics.oop.model.animal;

public class RegularGenome extends AbstractGenotype {
    public RegularGenome(int n){
        super(n);
    }

    public RegularGenome(int n, int minMutations, int maxMutations, Animal parentA, Animal parentB){
        super(n, minMutations, maxMutations, parentA, parentB);
    }

    @Override
    public void mutate(){
        int position = random.nextInt(this.getGenes().size());
        int newGen = random.nextInt(GENOTYPE_NUMBER);
        while(newGen == this.getGenes().get(position)){
            newGen = random.nextInt(GENOTYPE_NUMBER);
        }
        this.getGenes().set(position,newGen);
    }
}
