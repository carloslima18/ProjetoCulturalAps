<?php

namespace app\models;

use Yii;

/**
 * This is the model class for table "avaliacaopubpesq".
 *
 * @property int $id
 * @property int $nota
 * @property int $idpubpesq
 *
 * @property Publicacaopesq $pubpesq
 */
class Avaliacaopubpesq extends \yii\db\ActiveRecord
{
    /**
     * @inheritdoc
     */
    public static function tableName()
    {
        return 'avaliacaopubpesq';
    }

    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['nota', 'idpubpesq'], 'default', 'value' => null],
            [['nota', 'idpubpesq'], 'integer'],
            [['idpubpesq'], 'exist', 'skipOnError' => true, 'targetClass' => Publicacaopesq::className(), 'targetAttribute' => ['idpubpesq' => 'id']],
        ];
    }

    /**
     * @inheritdoc
     */
    public function attributeLabels()
    {
        return [
            'id' => 'ID',
            'nota' => 'Nota',
            'idpubpesq' => 'Publicação/pesquisador',
        ];
    }

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getPubpesq()
    {
        return $this->hasOne(Publicacaopesq::className(), ['id' => 'idpubpesq']);
    }
}
