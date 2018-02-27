<?php

namespace app\models;

use Yii;

/**
 * This is the model class for table "avaliacaopubuser".
 *
 * @property int $id
 * @property int $nota
 * @property int $idpubuser
 *
 * @property Publicacaouser $pubuser
 */
class Avaliacaopubuser extends \yii\db\ActiveRecord
{
    /**
     * @inheritdoc
     */
    public static function tableName()
    {
        return 'avaliacaopubuser';
    }

    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['nota', 'idpubuser'], 'default', 'value' => null],
            [['nota', 'idpubuser'], 'integer'],
            [['idpubuser'], 'exist', 'skipOnError' => true, 'targetClass' => Publicacaouser::className(), 'targetAttribute' => ['idpubuser' => 'id']],
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
            'idpubuser' => 'Publicação/user',
        ];
    }

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getPubuser()
    {
        return $this->hasOne(Publicacaouser::className(), ['id' => 'idpubuser']);
    }
}
