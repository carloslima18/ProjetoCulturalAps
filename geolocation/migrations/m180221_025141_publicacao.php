<?php

use yii\db\Migration;

/**
 * Class m180221_025141_publicacao
 */
class m180221_025141_publicacao extends Migration
{
    /**
     * @inheritdoc
     */
    public function safeUp()
    {
        $this->execute('create table publicacao(
                    id serial,
                    nome text NOT NULL,
                    redesocial text,
                    endereco text NOT NULL,
                    contato text NOT NULL,
                    atvexercida text NOT NULL,
                    categoria text NOT NULL,
                    latitude double precision,
                    longitude double precision,
                    geo_gps geometry,
                    primary key(id),
                    img1 text,
                    img2 text,
                    img3 text,
                    img4 text,
                    fk_user integer
                );'
        );



    }

    /**
     * @inheritdoc
     */
    public function safeDown()
    {
        $this->execute('drop table publicacao cascade;');

        echo "m180221_025141_publicacao cannot be reverted.\n";

        return false;
    }

    /*
    // Use up()/down() to run migration code without a transaction.
    public function up()
    {

    }

    public function down()
    {
        echo "m180221_025141_publicacao cannot be reverted.\n";

        return false;
    }
    */
}
